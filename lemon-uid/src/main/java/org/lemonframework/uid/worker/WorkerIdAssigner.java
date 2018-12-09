package org.lemonframework.uid.worker;

/**
 * Represents a worker id assigner for {@link org.lemonframework.uid.impl.DefaultUidGenerator}
 * 
 * @author yutianbao
 */
public interface WorkerIdAssigner {

    /**
     * Assign worker id for {@link org.lemonframework.uid.impl.DefaultUidGenerator}
     * 
     * @return assigned worker id
     */
    long assignWorkerId();

}
