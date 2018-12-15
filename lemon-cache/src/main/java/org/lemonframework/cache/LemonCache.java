package org.lemonframework.cache;

/**
 * cache.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class LemonCache implements LemonCacheClient {

    protected final Config config;

    protected LemonCache(Config config) {
        this.config = config;
//        Config configCopy = new Config(config);

//        connectionManager = ConfigSupport.createConnectionManager(configCopy);
//        evictionScheduler = new EvictionScheduler(connectionManager.getCommandExecutor());
    }

    /**
     * Create sync/async cache instance with default config
     *
     * @return Redisson instance
     */
    public static LemonCacheClient create() {
        Config config = new Config();
//        config.useSingleServer()
//                .setTimeout(1000000)
//                .setAddress("redis://127.0.0.1:6379");
//        config.useMasterSlaveConnection().setMasterAddress("127.0.0.1:6379").addSlaveAddress("127.0.0.1:6389").addSlaveAddress("127.0.0.1:6399");
//        config.useSentinelConnection().setMasterName("mymaster").addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379");
//        config.useClusterServers().addNodeAddress("127.0.0.1:7000");
        return create(config);
    }

    /**
     * Create sync/async cache instance with provided config
     *
     * @param config for cache
     * @return Cache instance
     */
    public static LemonCacheClient create(Config config) {
        LemonCache lemonCache = new LemonCache(config);
//        if (config.isReferenceEnabled()) {
//            redisson.enableRedissonReferenceSupport();
//        }
        return lemonCache;
    }

}
