package org.lemonframework.old.cache.lettuce;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.lemonframework.old.cache.Cache;
import org.lemonframework.old.cache.CacheChannel;
import org.lemonframework.old.cache.CacheExpiredListener;
import org.lemonframework.old.cache.CacheObject;
import org.lemonframework.old.cache.CacheProvider;
import org.lemonframework.old.cache.CacheProviderHolder;
import org.lemonframework.old.cache.ClusterCache;
import org.lemonframework.old.cache.Command;
import org.lemonframework.old.cache.cluster.ClusterPolicy;

/**
 * 使用 Lettuce 进行 Redis 的操作
 * <p>
 * 配置信息：
 * <p>
 * lettuce.namespace =
 * lettuce.storage = generic
 * lettuce.scheme = redis|rediss|redis-sentinel
 * lettuce.hosts = 127.0.0.1:6379
 * lettuce.password =
 * lettuce.database = 0
 * lettuce.sentinelMasterId =
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class LettuceCacheProvider extends RedisPubSubAdapter<String, String> implements CacheProvider, ClusterPolicy {

    //命令源标识，随机生成，每个节点都有唯一标识
    private int LOCAL_COMMAND_ID = Command.genRandomSrc();

    private static final LettuceByteCodec codec = new LettuceByteCodec();

    private static AbstractRedisClient redisClient;
    GenericObjectPool<StatefulConnection<String, byte[]>> pool;
    private StatefulRedisPubSubConnection<String, String> pubsub_subscriber;
    private String storage;

    private CacheProviderHolder holder;

    private String channel;
    private String namespace;

    private final ConcurrentHashMap<String, ClusterCache> regions = new ConcurrentHashMap();

    @Override
    public String name() {
        return "lettuce";
    }

    @Override
    public int level() {
        return CacheObject.ClUSTER;
    }

    @Override
    public boolean isLocalCommand(Command cmd) {
        return cmd.getSrc() == LOCAL_COMMAND_ID;
    }

    @Override
    public void start(Properties props) {
        this.namespace = props.getProperty("namespace");
        this.storage = props.getProperty("storage", "hash");
        this.channel = props.getProperty("channel", "lemoncache");

        String scheme = props.getProperty("scheme", "redis");
        String hosts = props.getProperty("hosts", "127.0.0.1:6379");
        String password = props.getProperty("password");
        int database = Integer.parseInt(props.getProperty("database", "0"));
        String sentinelMasterId = props.getProperty("sentinelMasterId");

        boolean isCluster = false;
        if ("redis-cluster".equalsIgnoreCase(scheme)) {
            scheme = "redis";
            isCluster = true;
        }

        String redis_url = String.format("%s://%s@%s/%d#%s", scheme, password, hosts, database, sentinelMasterId);

        redisClient = isCluster ? RedisClusterClient.create(redis_url) : RedisClient.create(redis_url);

        //connection pool configurations
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(Integer.parseInt(props.getProperty("maxTotal", "100")));
        poolConfig.setMaxIdle(Integer.parseInt(props.getProperty("maxIdle", "10")));
        poolConfig.setMinIdle(Integer.parseInt(props.getProperty("minIdle", "10")));

        pool = ConnectionPoolSupport.createGenericObjectPool(() -> {
            if (redisClient instanceof RedisClient) {
                return ((RedisClient) redisClient).connect(codec);
            } else if (redisClient instanceof RedisClusterClient) {
                return ((RedisClusterClient) redisClient).connect(codec);
            }
            return null;
        }, poolConfig);
    }

    @Override
    public void stop() {
        pool.close();
        regions.clear();
        redisClient.shutdown();
    }

    @Override
    public Cache buildCache(String region, CacheExpiredListener listener) {
        return regions.computeIfAbsent(this.namespace + ":" + region, v -> "hash".equalsIgnoreCase(this.storage) ?
                new LettuceHashCache(this.namespace, region, pool) :
                new LettuceGenericCache(this.namespace, region, pool));
    }

    @Override
    public Cache buildCache(String region, long timeToLiveInSeconds, CacheExpiredListener listener) {
        return buildCache(region, listener);
    }

    @Override
    public Collection<CacheChannel.Region> regions() {
        return Collections.emptyList();
    }

    /**
     * 删除本地某个缓存条目
     *
     * @param region 区域名称
     * @param keys   缓存键值
     */
    @Override
    public void evict(String region, String... keys) {
        holder.getLocalCache(region).evict(keys);
    }

    /**
     * 清除本地整个缓存区域
     *
     * @param region 区域名称
     */
    @Override
    public void clear(String region) {
        holder.getLocalCache(region).clear();
    }

    /**
     * Get PubSub connection
     *
     * @return connection instance
     */
    private StatefulRedisPubSubConnection pubsub() {
        if (redisClient instanceof RedisClient) {
            return ((RedisClient) redisClient).connectPubSub();
        } else if (redisClient instanceof RedisClusterClient) {
            return ((RedisClusterClient) redisClient).connectPubSub();
        }
        return null;
    }

    @Override
    public void connect(Properties props, CacheProviderHolder holder) {
        long ct = System.currentTimeMillis();
        this.holder = holder;
        this.channel = props.getProperty("channel", "j2cache");
        this.publish(Command.join());

        this.pubsub_subscriber = this.pubsub();
        this.pubsub_subscriber.addListener(this);
        RedisPubSubAsyncCommands<String, String> async = this.pubsub_subscriber.async();
        async.subscribe(this.channel);

        log.info("Connected to redis channel:{}, time {}ms.", this.channel, System.currentTimeMillis() - ct);
    }

    @Override
    public void message(String channel, String message) {
        Command cmd = Command.parse(message);
        handleCommand(cmd);
    }

    @Override
    public void publish(Command cmd) {
        cmd.setSrc(LOCAL_COMMAND_ID);
        try (StatefulRedisPubSubConnection<String, String> connection = this.pubsub()) {
            RedisPubSubCommands<String, String> sync = connection.sync();
            sync.publish(this.channel, cmd.json());
        }
    }

    @Override
    public void disconnect() {
        try {
            this.publish(Command.quit());
            super.unsubscribed(this.channel, 1);
        } finally {
            this.pubsub_subscriber.close();
        }
    }
}
