package org.lemonframework.extra.template2;

/**
 * 引擎接口，通过实现此接口从而使用对应的模板引擎.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public interface Engine {
    Template getTemplate(String resource);
}
