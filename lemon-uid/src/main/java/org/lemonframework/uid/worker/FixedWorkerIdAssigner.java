package org.lemonframework.uid.worker;

/**
 * 固定的分配workerID.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class FixedWorkerIdAssigner implements WorkerIdAssigner {
    @Override
    public long assignWorkerId() {
        return 2;
    }
}
