package org.lemonframework.cache;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lemonframework.cache.cluster.ClusterPolicy;
import org.lemonframework.cache.cluster.ClusterPolicyFactory;
import org.lemonframework.cache.util.SerializationUtils;
import org.lemonframework.log.Log;
import org.lemonframework.log.LogFactory;

/**
 * 使用自定义配置构建 J2Cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class LemonCacheBuilder {

    private final static Log log = LogFactory.get(LemonCacheBuilder.class);

    private CacheChannel channel;
    private CacheProviderHolder holder;
    //不同的广播策略
    private ClusterPolicy policy;
    private AtomicBoolean opened = new AtomicBoolean(false);
    private LemonCacheConfig config;

    private LemonCacheBuilder(LemonCacheConfig config) {
        this.config = config;
    }

    /**
     * 初始化 LemonCache，这是一个很重的操作，请勿重复执行.
     *
     * @param config lemon config instance
     * @return LemonCacheBuilder instance
     */
    public final static LemonCacheBuilder init(LemonCacheConfig config) {
        return new LemonCacheBuilder(config);
    }

    /**
     * 返回缓存操作接口
     *
     * @return CacheChannel
     */
    public CacheChannel getChannel() {
        if (this.channel == null || !this.opened.get()) {
            synchronized (LemonCacheBuilder.class) {
                if (this.channel == null || !this.opened.get()) {
                    this.initFromConfig(config);
                    /* 初始化缓存接口 */
                    this.channel = new CacheChannel(config, holder) {
                        @Override
                        public void sendClearCmd(String region) {
                            policy.sendClearCmd(region);
                        }

                        @Override
                        public void sendEvictCmd(String region, String... keys) {
                            policy.sendEvictCmd(region, keys);
                        }

                        @Override
                        public void close() {
                            super.close();
                            policy.disconnect();
                            holder.shutdown();
                            opened.set(false);
                        }
                    };
                    this.opened.set(true);
                }
            }
        }
        return this.channel;
    }

    /**
     * 关闭 LemonCache
     */
    public void close() {
        this.channel.close();
        this.channel = null;
    }

    /**
     * 加载配置
     *
     * @return
     * @throws IOException
     */
    private void initFromConfig(LemonCacheConfig config) {
        SerializationUtils.init(config.getSerialization(), config.getSubProperties(config.getSerialization()));
        //初始化两级的缓存管理
        this.holder = CacheProviderHolder.init(config, (region, key) -> {
            //当一级缓存中的对象失效时，自动清除二级缓存中的数据
            ClusterCache level2 = this.holder.getLevel2Cache(region);
            level2.evict(key);
            if (!level2.supportTTL()) {
                //再一次清除一级缓存是为了避免缓存失效时再次从 L2 获取到值
                this.holder.getLevel1Cache(region).evict(key);
            }
            log.debug("Local cache object expired, evict level 2 cache object [{},{}]", region, key);
            if (policy != null) {
                policy.sendEvictCmd(region, key);
            }
        });

        policy = ClusterPolicyFactory.init(holder, config.getBroadcast(), config.getBroadcastProperties());
        log.info("Using cluster policy : {}", policy.getClass().getName());
    }

}
