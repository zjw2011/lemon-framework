package org.lemonframework.cache.lettuce;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.BaseRedisCommands;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.lemonframework.cache.CacheException;
import org.lemonframework.cache.ClusterCache;

/**
 * Lettuce 的基类，封装了普通 Redis 连接和集群 Redis 连接的差异
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public abstract class LettuceCache implements ClusterCache {

    protected String namespace;
    protected String region;
    protected GenericObjectPool<StatefulConnection<String, byte[]>> pool;

    protected StatefulConnection connect() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    protected BaseRedisCommands sync(StatefulConnection conn) {
        if(conn instanceof StatefulRedisClusterConnection) {
            return ((StatefulRedisClusterConnection) conn).sync();
        }
        else if(conn instanceof StatefulRedisConnection) {
            return ((StatefulRedisConnection) conn).sync();
        }
        return null;
    }

}
