package org.lemonframework.cache;

import java.io.Closeable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Cache Channel, the LemonCache methods explored to developers.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class CacheChannel implements Closeable, AutoCloseable {

    private static final Map<String, Object> _g_keyLocks = new ConcurrentHashMap<>();
    private LemonCacheConfig config;
    private CacheProviderHolder holder;
    private boolean defaultCacheNullObject;
    private boolean closed;

    public CacheChannel(LemonCacheConfig config, CacheProviderHolder holder) {
        this.config = config;
        this.holder = holder;
        this.defaultCacheNullObject = config.isDefaultCacheNullObject();
        this.closed = false;
    }

    private NullObject newNullObject() {
        return new NullObject();
    }

    /**
     * <p>Just for Inner Use.</p>
     * <p>
     * <p>To clear the whole region when received this event .</p>
     *
     * @param region Cache region name
     */
    protected abstract void sendClearCmd(String region);

    /**
     * <p>Just for Inner Use.</p>
     * <p>
     * <p>To remove cached data when received this event .</p>
     *
     * @param region Cache region name
     * @param keys   Cache data key
     */
    protected abstract void sendEvictCmd(String region, String... keys);

    /**
     * 读取缓存（用户无需判断返回的对象是否为空）
     *
     * @param region          Cache region name
     * @param key             Cache data key
     * @param cacheNullObject 是否缓存空对象
     * @return cache object
     */
    public CacheObject get(String region, String key, boolean... cacheNullObject) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        CacheObject obj = new CacheObject(region, key, CacheObject.LOCAL);
        obj.setValue(holder.getLocalCache(region).get(key));
        if (obj.rawValue() != null) {
            return obj;
        }

        String lock_key = key + '%' + region;
        synchronized (_g_keyLocks.computeIfAbsent(lock_key, v -> new Object())) {
            obj.setValue(holder.getLocalCache(region).get(key));
            if (obj.rawValue() != null) {
                return obj;
            }

            try {
                obj.setLevel(CacheObject.ClUSTER);
                obj.setValue(holder.getClusterCache(region).get(key));
                if (obj.rawValue() != null) {
                    holder.getLocalCache(region).put(key, obj.rawValue());
                } else {
                    boolean cacheNull = (cacheNullObject.length > 0) ? cacheNullObject[0] : defaultCacheNullObject;
                    if (cacheNull) {
                        set(region, key, newNullObject(), true);
                    }
                }
            } finally {
                _g_keyLocks.remove(lock_key);
            }
        }

        return obj;
    }

    /**
     * 支持外部数据自动加载的缓存方法
     *
     * @param region          Cache region name
     * @param key             Cache data key
     * @param loader          data loader
     * @param cacheNullObject true if you need to cache null object
     * @return cache object
     */
    public CacheObject get(String region, String key, Function<String, Object> loader, boolean... cacheNullObject) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        CacheObject cache = get(region, key, false);

        if (cache.rawValue() != null) {
            return cache;
        }

        String lock_key = key + '@' + region;
        synchronized (_g_keyLocks.computeIfAbsent(lock_key, v -> new Object())) {
            cache = get(region, key, false);

            if (cache.rawValue() != null) {
                return cache;
            }

            try {
                Object obj = loader.apply(key);
                boolean cacheNull = (cacheNullObject.length > 0) ? cacheNullObject[0] : defaultCacheNullObject;
                set(region, key, obj, cacheNull);
                cache = new CacheObject(region, key, CacheObject.LEVEL_OUTER, obj);
            } finally {
                _g_keyLocks.remove(lock_key);
            }
        }
        return cache;
    }

    /**
     * 批量读取缓存中的对象(用户无需判断返回的对象是否为空).
     *
     * @param region Cache region name
     * @param keys   cache keys
     * @return cache object
     */
    public Map<String, CacheObject> get(String region, Collection<String> keys) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        final Map<String, Object> objs = holder.getLocalCache(region).get(keys);
        List<String> level2Keys = keys.stream().filter(k -> !objs.containsKey(k) || objs.get(k) == null).collect(Collectors.toList());
        Map<String, CacheObject> results = objs.entrySet().stream().filter(p -> p.getValue() != null).collect(
                Collectors.toMap(
                        p -> p.getKey(),
                        p -> new CacheObject(region, p.getKey(), CacheObject.LOCAL, p.getValue())
                )
        );

        Map<String, Object> objsClusterCache = holder.getClusterCache(region).get(level2Keys);
        objsClusterCache.forEach((k, v) -> {
            results.put(k, new CacheObject(region, k, CacheObject.ClUSTER, v));
            if (v != null) {
                holder.getLocalCache(region).put(k, v);
            }
        });

        return results;
    }

    /**
     * 使用数据加载器的批量缓存读取.
     *
     * @param region          Cache region name
     * @param keys            cache keys
     * @param loader          data loader
     * @param cacheNullObject true if you need to cache null object
     * @return multiple cache data
     */
    public Map<String, CacheObject> get(String region, Collection<String> keys, Function<String, Object> loader, boolean... cacheNullObject) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        Map<String, CacheObject> results = get(region, keys);
        results.entrySet().stream().filter(e -> e.getValue().rawValue() == null).forEach(e -> {
            String lock_key = e.getKey() + '@' + region;
            synchronized (_g_keyLocks.computeIfAbsent(lock_key, v -> new Object())) {
                CacheObject cache = get(region, e.getKey(), false);
                if (cache.rawValue() == null) {
                    try {
                        Object obj = loader.apply(e.getKey());
                        boolean cacheNull = (cacheNullObject.length > 0) ? cacheNullObject[0] : defaultCacheNullObject;
                        set(region, e.getKey(), obj, cacheNull);
                        e.getValue().setValue(obj);
                        e.getValue().setLevel(CacheObject.LEVEL_OUTER);
                    } finally {
                        _g_keyLocks.remove(lock_key);
                    }
                } else {
                    e.setValue(cache);
                }
            }
        });
        return results;
    }

    /**
     * 判断某个缓存键是否存在.
     *
     * @param region Cache region name
     * @param key    cache key
     * @return true if key exists
     */
    public boolean exists(String region, String key) {
        return check(region, key) > 0;
    }

    /**
     * 判断某个key存在于哪级的缓存中
     *
     * @param region cache region
     * @param key    cache key
     * @return 0(不存在), 1(一级), 2(二级)
     */
    public int check(String region, String key) {
        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        if (holder.getLocalCache(region).exists(key)) {
            return 1;
        }
        if (holder.getClusterCache(region).exists(key)) {
            return 2;
        }
        return 0;
    }

    /**
     * Write data to J2Cache
     *
     * @param region: Cache Region name
     * @param key:    Cache key
     * @param value:  Cache value
     */
    public void set(String region, String key, Object value) {
        set(region, key, value, defaultCacheNullObject);
    }

    /**
     * Write data to LemonCache.
     *
     * @param region:         Cache Region name
     * @param key:            Cache key
     * @param value:          Cache value
     * @param cacheNullObject if allow cache null object
     */
    public void set(String region, String key, Object value, boolean cacheNullObject) {

        if (!cacheNullObject && value == null) {
            return;
        }

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        try {
            LocalCache localCache = holder.getLocalCache(region);
            localCache.put(key, (value == null && cacheNullObject) ? newNullObject() : value);
            ClusterCache clusterCache = holder.getClusterCache(region);
            if (config.isSyncTtlToRedis()) {
                clusterCache.put(key, (value == null && cacheNullObject) ? newNullObject() : value, localCache.ttl());
            }
            else {
                clusterCache.put(key, (value == null && cacheNullObject) ? newNullObject() : value);
            }
        } finally {
            // 清除原有的一级缓存的内容
            this.sendEvictCmd(region, key);
        }
    }

    /**
     * Write data to j2cache with expired setting
     * <p>
     * <strong>注意：强烈不推荐使用带 TTL 的 set 方法，所有的缓存 TTL 都应该预先配置好，避免多个节点的缓存 Region 配置不同步</strong>
     *
     * @param region              Cache Region name
     * @param key                 Cache Key
     * @param value               Cache value
     * @param timeToLiveInSeconds cache expired in second
     */
    public void set(String region, String key, Object value, long timeToLiveInSeconds) {
        set(region, key, value, timeToLiveInSeconds, defaultCacheNullObject);
    }

    /**
     * Write data to localcache with expired setting
     * <p>
     * <strong>注意：强烈不推荐使用带 TTL 的 set 方法，所有的缓存 TTL 都应该预先配置好，避免多个节点的缓存 Region 配置不同步</strong>
     *
     * @param region              Cache Region name
     * @param key                 Cache Key
     * @param value               Cache value
     * @param timeToLiveInSeconds cache expired in second
     * @param cacheNullObject     if allow cache null object
     */
    public void set(String region, String key, Object value, long timeToLiveInSeconds, boolean cacheNullObject) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        if (!cacheNullObject && value == null) {
            return;
        }

        if (timeToLiveInSeconds <= 0) {
            set(region, key, value, cacheNullObject);
        }
        else {
            try {
                holder.getLocalCache(region, timeToLiveInSeconds).put(key, (value == null && cacheNullObject) ? newNullObject() : value);
                ClusterCache clusterCache = holder.getClusterCache(region);
                if (config.isSyncTtlToRedis()) {
                    clusterCache.put(key, (value == null && cacheNullObject) ? newNullObject() : value, timeToLiveInSeconds);
                }
                else {
                    clusterCache.put(key, (value == null && cacheNullObject) ? newNullObject() : value);
                }
            } finally {
                //清除原有的一级缓存的内容
                this.sendEvictCmd(region, key);
            }
        }
    }

    /**
     * 批量插入数据
     *
     * @param region   Cache Region name
     * @param elements Cache Elements
     */
    public void set(String region, Map<String, Object> elements) {
        set(region, elements, defaultCacheNullObject);
    }

    /**
     * 批量插入数据
     *
     * @param region          Cache Region name
     * @param elements        Cache Elements
     * @param cacheNullObject if allow cache null object
     */
    public void set(String region, Map<String, Object> elements, boolean cacheNullObject) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        try {
            if (cacheNullObject && elements.containsValue(null)) {
                Map<String, Object> newElems = new HashMap<>();
                newElems.putAll(elements);
                newElems.forEach((k, v) -> {
                    if (v == null) {
                        newElems.put(k, newNullObject());
                    }
                });
                LocalCache localCache = holder.getLocalCache(region);
                localCache.put(newElems);
                if (config.isSyncTtlToRedis()) {
                    holder.getClusterCache(region).put(newElems, localCache.ttl());
                }
                else {
                    holder.getClusterCache(region).put(newElems);
                }
            } else {
                LocalCache level1 = holder.getLocalCache(region);
                level1.put(elements);
                if (config.isSyncTtlToRedis()) {
                    holder.getClusterCache(region).put(elements, level1.ttl());
                }
                else {
                    holder.getClusterCache(region).put(elements);
                }
            }
        } finally {
            //广播
            this.sendEvictCmd(region, elements.keySet().stream().toArray(String[]::new));
        }
    }

    /**
     * 带失效时间的批量缓存数据插入
     * <p>
     * <strong>注意：强烈不推荐使用带 TTL 的 set 方法，所有的缓存 TTL 都应该预先配置好，避免多个节点的缓存 Region 配置不同步</strong>
     *
     * @param region              Cache Region name
     * @param elements            Cache Elements
     * @param timeToLiveInSeconds cache expired in second
     */
    public void set(String region, Map<String, Object> elements, long timeToLiveInSeconds) {
        set(region, elements, timeToLiveInSeconds, defaultCacheNullObject);
    }

    /**
     * 带失效时间的批量缓存数据插入
     * <p>
     * <strong>注意：强烈不推荐使用带 TTL 的 set 方法，所有的缓存 TTL 都应该预先配置好，避免多个节点的缓存 Region 配置不同步</strong>
     *
     * @param region              Cache Region name
     * @param elements            Cache Elements
     * @param timeToLiveInSeconds cache expired in second
     * @param cacheNullObject     if allow cache null object
     */
    public void set(String region, Map<String, Object> elements, long timeToLiveInSeconds, boolean cacheNullObject) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        if (timeToLiveInSeconds <= 0) {
            set(region, elements, cacheNullObject);
        }
        else {
            try {
                if (cacheNullObject && elements.containsValue(null)) {
                    Map<String, Object> newElems = new HashMap<>();
                    newElems.putAll(elements);
                    newElems.forEach((k, v) -> {
                        if (v == null) {
                            newElems.put(k, newNullObject());
                        }
                    });
                    holder.getLocalCache(region, timeToLiveInSeconds).put(newElems);
                    if (config.isSyncTtlToRedis()) {
                        holder.getClusterCache(region).put(newElems, timeToLiveInSeconds);
                    }
                    else {
                        holder.getClusterCache(region).put(newElems);
                    }
                } else {
                    holder.getLocalCache(region, timeToLiveInSeconds).put(elements);
                    if (config.isSyncTtlToRedis()) {
                        holder.getClusterCache(region).put(elements, timeToLiveInSeconds);
                    }
                    else {
                        holder.getClusterCache(region).put(elements);
                    }
                }
            } finally {
                //广播
                this.sendEvictCmd(region, elements.keySet().stream().toArray(String[]::new));
            }
        }
    }

    /**
     * Remove cached data in J2Cache
     *
     * @param region: Cache Region name
     * @param keys:   Cache key
     */
    public void evict(String region, String... keys) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        try {
            //先清比较耗时的二级缓存，再清一级缓存
            holder.getClusterCache(region).evict(keys);
            holder.getLocalCache(region).evict(keys);
        } finally {
            this.sendEvictCmd(region, keys); //发送广播
        }
    }

    /**
     * Clear the cache
     *
     * @param region: Cache region name
     */
    public void clear(String region) {

        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        try {
            //先清比较耗时的二级缓存，再清一级缓存
            holder.getClusterCache(region).clear();
            holder.getLocalCache(region).clear();
        } finally {
            this.sendClearCmd(region);
        }
    }

    /**
     * 返回所有的缓存区域
     *
     * @return all the regions
     */
    public Collection<Region> regions() {
        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        return holder.regions();
    }

    /**
     * 删除缓存 Region
     *
     * @param region Cache Region Name
     */
    public void removeRegion(String region) {
        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        holder.getL1Provider().removeCache(region);
    }

    /**
     * <p>Get cache region keys</p>.
     * <p><strong>Notice: ehcache3 not support keys</strong></p>
     *
     * @param region: Cache region name
     * @return key list
     */
    public Collection<String> keys(String region) {
        if (closed) {
            throw new IllegalStateException("CacheChannel closed");
        }

        Set<String> keys = new HashSet<>();
        keys.addAll(holder.getLocalCache(region).keys());
        keys.addAll(holder.getClusterCache(region).keys());
        return keys;
    }

    /**
     * Close J2Cache
     */
    @Override
    public void close() {
        this.closed = true;
    }

    /**
     * 获取一级缓存接口
     *
     * @return 返回一级缓存的 CacheProvider 实例
     */
    public CacheProvider getL1Provider() {
        return holder.getL1Provider();
    }

    /**
     * <p>获取二级缓存的接口，该方法可用来直接获取 J2Cache 底层的 Redis 客户端实例</p>
     * <p>方法如下：</p>
     * <code>
     * CacheChannel channel = J2Cache.getChannel();
     * RedisClient client = ((RedisCacheProvider)channel.getL2Provider()).getRedisClient();
     * try {
     * client.get().xxxxx(...);
     * } finally {
     * client.release();
     * }
     * </code>
     *
     * @return 返回二级缓存的 CacheProvider 实例
     */
    public CacheProvider getL2Provider() {
        return holder.getL2Provider();
    }

    /**
     * Cache Region Define.
     */
    public static class Region {

        private String name;
        private long size;
        private long ttl;

        public Region(String name, long size, long ttl) {
            this.name = name;
            this.size = size;
            this.ttl = ttl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getTtl() {
            return ttl;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }

        @Override
        public String toString() {
            return String.format("[%s,size:%d,ttl:%d]", name, size, ttl);
        }
    }

}
