package org.lemonframework.cache;

/**
 * 本地缓存接口.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface LocalCache extends Cache {
    /**
     * 返回该缓存区域的 TTL 设置（单位：秒）
     * @return true if cache support ttl setting
     */
    long ttl();

    /**
     * 返回该缓存区域中，内存存储对象的最大数量
     * @return cache size in memory
     */
    long size();
}
