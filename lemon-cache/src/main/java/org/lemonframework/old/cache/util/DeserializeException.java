package org.lemonframework.old.cache.util;

/**
 * 反序列化的对象兼容异常.
 * @author jiawei zhang
 * @since 0.0.1
 */
public class DeserializeException extends RuntimeException {

    public DeserializeException(String message) {
        super(message);
    }
}
