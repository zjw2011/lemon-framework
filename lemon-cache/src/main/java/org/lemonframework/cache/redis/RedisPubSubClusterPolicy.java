package org.lemonframework.cache.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.lemonframework.cache.CacheProviderHolder;
import org.lemonframework.cache.Command;
import org.lemonframework.cache.cluster.ClusterPolicy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Pool;

/**
 * 使用 Redis 的订阅和发布进行集群中的节点通知.
 * 该策略器使用 lemon.cache.properties 中的 redis 配置自行保持两个到 redis 的连接用于发布和订阅消息（并在失败时自动重连）
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class RedisPubSubClusterPolicy extends JedisPubSub implements ClusterPolicy {

    private final static Logger log = LoggerFactory.getLogger(RedisPubSubClusterPolicy.class);

    //命令源标识，随机生成，每个节点都有唯一标识
    private int LOCAL_COMMAND_ID = Command.genRandomSrc();

    private Pool<Jedis> client;
    private String channel;
    private CacheProviderHolder holder;

    public RedisPubSubClusterPolicy(String channel, Properties props) {
        this.channel = channel;
        int timeout = Integer.parseInt((String) props.getOrDefault("timeout", "2000"));
        String password = props.getProperty("password");
        if (password != null && password.trim().length() == 0) {
            password = null;
        }

        int database = Integer.parseInt(props.getProperty("database", "0"));

        JedisPoolConfig config = RedisUtils.newPoolConfig(props, null);

        String node = props.getProperty("channel.host");
        if (node == null || node.trim().length() == 0) {
            node = props.getProperty("hosts");
        }

        if ("sentinel".equalsIgnoreCase(props.getProperty("mode"))) {
            Set<String> hosts = new HashSet();
            hosts.addAll(Arrays.asList(node.split(",")));
            String masterName = props.getProperty("cluster_name", "lemoncache");
            this.client = new JedisSentinelPool(masterName, hosts, config, timeout, password, database);
        } else {
            //取第一台主机
            node = node.split(",")[0];
            String[] infos = node.split(":");
            String host = infos[0];
            int port = (infos.length > 1) ? Integer.parseInt(infos[1]) : 6379;
            this.client = new JedisPool(config, host, port, timeout, password, database);
        }
    }

    @Override
    public boolean isLocalCommand(Command cmd) {
        return cmd.getSrc() == LOCAL_COMMAND_ID;
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
     * 清除本地整个缓存区域.
     *
     * @param region 区域名称
     */
    @Override
    public void clear(String region) {
        holder.getLocalCache(region).clear();
    }

    /**
     * 加入 Redis 的发布订阅频道
     */
    @Override
    public void connect(Properties props, CacheProviderHolder holder) {
        long ct = System.currentTimeMillis();
        this.holder = holder;

        this.publish(Command.join());

        //当 Redis 重启会导致订阅线程断开连接，需要进行重连
        Thread subscribeThread = new Thread(() -> {
            while (!client.isClosed()) {
                try (Jedis jedis = client.getResource()) {
                    jedis.subscribe(this, channel);
                    log.info("Disconnect to redis channel: {}", channel);
                    break;
                } catch (JedisConnectionException e) {
                    log.error("Failed connect to redis, reconnect it.", e);
                    if (!client.isClosed()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            break;
                        }
                    }
                }
            }
        }, "RedisSubscribeThread");

        subscribeThread.setDaemon(true);
        subscribeThread.start();

        log.info("Connected to redis channel:{}, time {} ms.", channel, (System.currentTimeMillis() - ct));
    }

    /**
     * 退出 Redis 发布订阅频道
     */
    @Override
    public void disconnect() {
        try {
            this.publish(Command.quit());
            if (this.isSubscribed()) {
                this.unsubscribe();
            }
        } finally {
            this.client.close();
            //subscribeThread will auto terminate
        }
    }

    @Override
    public void publish(Command cmd) {
        cmd.setSrc(LOCAL_COMMAND_ID);
        try (Jedis jedis = client.getResource()) {
            jedis.publish(channel, cmd.json());
        }
    }

    /**
     * 当接收到订阅频道获得的消息时触发此方法
     *
     * @param channel 频道名称
     * @param message 消息体
     */
    @Override
    public void onMessage(String channel, String message) {
        Command cmd = Command.parse(message);
        handleCommand(cmd);
    }

}
