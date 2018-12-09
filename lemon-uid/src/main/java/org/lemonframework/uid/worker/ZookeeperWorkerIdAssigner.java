package org.lemonframework.uid.worker;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.lemonframework.uid.exception.UidGenerateException;

/**
 * zookeeper模式WorkerID分配器,下载到本地.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class ZookeeperWorkerIdAssigner implements WorkerIdAssigner {
    /** 服务器地址 */
    private String servers = "127.0.0.1:2181";
    /** 初试时间 */
    private int baseSleepTimeMs = 1000;
    /** 重试次数 */
    private int maxRetries = 3;
    /** 会话超时时间  */
    private int sessionTimeoutMs = 5000;
    /** 结点路径 */
    private String path = "/lemon/uid";

    private CuratorFramework client;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void init() {
        //重试策略，初试时间1秒，重试10次
        RetryPolicy policy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        //通过工厂创建Curator
        client = CuratorFrameworkFactory.builder().connectString(servers)
                .sessionTimeoutMs(sessionTimeoutMs).retryPolicy(policy).build();
        //开启连接
        client.start();
    }

    /**
     * @see org.lemonframework.uid.worker.WorkerIdAssigner#assignWorkerId()
     */
    @Override
    public long assignWorkerId() {
        init();
        try {
            Stat stat = client.checkExists().forPath(path);
            if(stat == null) {
                client.create().creatingParentsIfNeeded().forPath(path);
            }
            stat = client.setData().forPath(path, new byte[0]);
            return stat.getVersion();
        } catch (Exception e) {
            throw new UidGenerateException("创建zookeeper节点失败", e);
        }
    }

    public void close() {
        client.close();
    }
}
