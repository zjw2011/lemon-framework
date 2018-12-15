package org.lemonframework.old.cache.cluster;

import java.util.Properties;

import org.lemonframework.old.cache.CacheProviderHolder;
import org.lemonframework.old.cache.Command;

/**
 * 实现空的集群通知策略
 * @author jiawei zhang
 * @since 0.0.1
 */
public class NoneClusterPolicy implements ClusterPolicy {

    private int LOCAL_COMMAND_ID = Command.genRandomSrc(); //命令源标识，随机生成，每个节点都有唯一标识

    @Override
    public boolean isLocalCommand(Command cmd) {
        return cmd.getSrc() == LOCAL_COMMAND_ID;
    }

    @Override
    public void connect(Properties props, CacheProviderHolder holder) {
    }

    @Override
    public void disconnect() {
    }

    @Override
    public void publish(Command cmd) {
    }

    @Override
    public void evict(String region, String... keys) {
    }

    @Override
    public void clear(String region) {
    }
}
