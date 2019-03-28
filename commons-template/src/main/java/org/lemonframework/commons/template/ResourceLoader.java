package org.lemonframework.commons.template;

/**
 * 资源加载方式.
 * COMPOSITE ==> File、ClassPath、Web-root、String
 * @author jiawei zhang
 * @since 0.0.1
 */
public enum ResourceLoader {
    CLASSPATH,
    FILE,
    WEB_ROOT,
    STRING,
    COMPOSITE
}