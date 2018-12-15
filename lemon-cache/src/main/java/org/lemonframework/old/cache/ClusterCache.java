package org.lemonframework.old.cache;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.lemonframework.old.cache.util.DeserializeException;
import org.lemonframework.old.cache.util.SerializationUtils;
import org.lemonframework.log.Log;
import org.lemonframework.log.LogFactory;

/**
 * 集群缓存.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface ClusterCache extends Cache {

    Log log = LogFactory.get(ClusterCache.class);

    /**
     * 是否支持缓存 TTL 的设置
     *
     * @return true/false if cache support ttl setting
     */
    default boolean supportTTL() {
        return false;
    }

    /**
     * 读取缓存数据字节数组
     *
     * @param key cache key
     * @return cache data
     */
    byte[] getBytes(String key);

    /**
     * 同时读取多个 Key
     *
     * @param keys multiple cache key
     * @return cache values
     */
    List<byte[]> getBytes(Collection<String> keys);

    /**
     * 设置缓存数据字节数组
     *
     * @param key   cache key
     * @param bytes cache data
     */
    void setBytes(String key, byte[] bytes);

    /**
     * 同时设置多个数据
     *
     * @param bytes cache data
     */
    void setBytes(Map<String, byte[]> bytes);

    /**
     * 设置缓存数据字节数组（带有效期）
     *
     * @param key                 cache key
     * @param bytes               cache data
     * @param timeToLiveInSeconds cache ttl
     */
    default void setBytes(String key, byte[] bytes, long timeToLiveInSeconds) {
        setBytes(key, bytes);
    }

    /**
     * 批量设置带 TTL 的缓存数据
     *
     * @param bytes               cache data
     * @param timeToLiveInSeconds cache ttl
     */
    default void setBytes(Map<String, byte[]> bytes, long timeToLiveInSeconds) {
        setBytes(bytes);
    }

    /**
     * 判断缓存数据是否存在
     *
     * @param key cache key
     * @return true if cache key exists in redis
     */
    @Override
    default boolean exists(String key) {
        return getBytes(key) != null;
    }

    /**
     * Return all keys
     *
     * @return 返回键的集合
     */
    @Override
    Collection<String> keys();

    /**
     * Remove items from the cache
     *
     * @param keys Cache key
     */
    @Override
    void evict(String... keys);

    /**
     * Clear the cache
     */
    @Override
    void clear();

    @Override
    default Object get(String key) {
        byte[] bytes = getBytes(key);
        try {
            return SerializationUtils.deserialize(bytes);
        } catch (DeserializeException e) {
            log.warn("Failed to deserialize object with key:" + key + ",message: " + e.getMessage());
            evict(key);
            return null;
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }

    @Override
    default Map<String, Object> get(Collection<String> keys) {
        Map<String, Object> results = new HashMap<>();
        if (keys.size() > 0) {
            List<byte[]> bytes = getBytes(keys);
            int i = 0;
            for (String key : keys) {
                try {
                    results.put(key, SerializationUtils.deserialize(bytes.get(i++)));
                } catch (DeserializeException e) {
                    log.warn("Failed to deserialize object with key:" + key + ",message: " + e.getMessage());
                    evict(key);
                    return null;
                } catch (IOException e) {
                    throw new CacheException(e);
                }
            }
        }
        return results;
    }

    @Override
    default void put(String key, Object value) {
        try {
            setBytes(key, SerializationUtils.serialize(value));
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }

    /**
     * 设置缓存数据的有效期
     *
     * @param key                 cache key
     * @param value               cache value
     * @param timeToLiveInSeconds cache ttl
     */
    default void put(String key, Object value, long timeToLiveInSeconds) {
        try {
            setBytes(key, SerializationUtils.serialize(value), timeToLiveInSeconds);
        } catch (IOException e) {
            throw new CacheException(e);
        }
    }

    @Override
    default void put(Map<String, Object> elements) {
        if (elements.size() > 0) {
            setBytes(elements.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(), p -> SerializationUtils.serializeWithoutException(p.getValue()))));
        }
    }

    default void put(Map<String, Object> elements, long timeToLiveInSeconds) {
        if (elements.size() > 0) {
            setBytes(elements.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(), p -> SerializationUtils.serializeWithoutException(p.getValue()))), timeToLiveInSeconds);
        }
    }
}
