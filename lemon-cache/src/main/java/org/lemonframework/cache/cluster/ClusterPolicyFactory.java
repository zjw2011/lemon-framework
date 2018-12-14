package org.lemonframework.cache.cluster;

import java.util.Properties;

import org.lemonframework.cache.CacheException;
import org.lemonframework.cache.CacheProviderHolder;
import org.lemonframework.cache.lettuce.LettuceCacheProvider;
import org.lemonframework.cache.redis.RedisPubSubClusterPolicy;

/**
 * 集群策略工厂
 * @author jiawei zhang
 * @since 0.0.1
 */
public class ClusterPolicyFactory {

    private ClusterPolicyFactory(){}

    /**
     * 初始化集群消息通知机制
     * @param broadcast  j2cache.broadcast value
     * @param props  broadcast configuations
     * @return ClusterPolicy instance
     */
    public final static ClusterPolicy init(CacheProviderHolder holder, String broadcast, Properties props) {

        ClusterPolicy policy;
        if ("redis".equalsIgnoreCase(broadcast)) {
            policy = ClusterPolicyFactory.redis(props, holder);
        }
        else if ("jgroups".equalsIgnoreCase(broadcast)) {
            policy = ClusterPolicyFactory.jgroups(props, holder);
        }
        else if ("lettuce".equalsIgnoreCase(broadcast)) {
            policy = ClusterPolicyFactory.lettuce(props, holder);
        }
        else if ("none".equalsIgnoreCase(broadcast)) {
            policy = new NoneClusterPolicy();
        }
        else {
            policy = ClusterPolicyFactory.custom(broadcast, props, holder);
        }

        return policy;
    }

    /**
     * 使用 Redis 订阅和发布机制，该方法只能调用一次
     * @param props 框架配置
     * @return 返回 Redis 集群策略的实例
     */
    private final static ClusterPolicy redis(Properties props, CacheProviderHolder holder) {
        String name = props.getProperty("channel");
        RedisPubSubClusterPolicy policy = new RedisPubSubClusterPolicy(name, props);
        policy.connect(props, holder);
        return policy;
    }

    /**
     * 使用 JGroups 组播机制
     * @param props 框架配置
     * @return 返回 JGroups 集群策略的实例
     */
    private final static ClusterPolicy jgroups(Properties props, CacheProviderHolder holder) {
        String name = props.getProperty("channel.name");
        JGroupsClusterPolicy policy = new JGroupsClusterPolicy(name, props);
        policy.connect(props, holder);
        return policy;
    }

    private final static ClusterPolicy lettuce(Properties props, CacheProviderHolder holder) {
        LettuceCacheProvider policy = new LettuceCacheProvider();
        policy.connect(props, holder);
        return policy;
    }

    /**
     * 加载自定义的集群通知策略
     * @param classname
     * @param props
     * @return
     */
    private final static ClusterPolicy custom(String classname, Properties props, CacheProviderHolder holder) {
        try {
            ClusterPolicy policy = (ClusterPolicy)Class.forName(classname).newInstance();
            policy.connect(props, holder);
            return policy;
        } catch (Exception e) {
            throw new CacheException("Failed in load custom cluster policy. class = " + classname, e);
        }
    }

}
