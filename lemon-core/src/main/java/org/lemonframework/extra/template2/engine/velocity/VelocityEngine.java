package org.lemonframework.extra.template2.engine.velocity;

import org.apache.velocity.app.Velocity;
import org.lemonframework.extra.template2.Engine;
import org.lemonframework.extra.template2.Template;
import org.lemonframework.extra.template2.TemplateConfig;

/**
 * Velocity模板引擎.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class VelocityEngine implements Engine {

    private org.apache.velocity.app.VelocityEngine engine;

    // --------------------------------------------------------------------------------- Constructor start.
    /**
     * 默认构造.
     * @since 0.0.1
     */
    public VelocityEngine() {
        this(new TemplateConfig());
    }

    /**
     * 构造
     *
     * @param config 模板配置
     */
    public VelocityEngine(TemplateConfig config) {
        this(createEngine(config));
    }


    /**
     * 构造.
     *
     * @param engine {@link org.apache.velocity.app.VelocityEngine}
     * @since 0.0.1
     */
    public VelocityEngine(org.apache.velocity.app.VelocityEngine engine) {
        this.engine = engine;
    }
    // --------------------------------------------------------------------------------- Constructor end.


    /**
     * 创建引擎
     *
     * @return {@link org.apache.velocity.app.VelocityEngine}
     * @since 0.0.1
     */
    private static org.apache.velocity.app.VelocityEngine createEngine(TemplateConfig config) {

        if (null == config) {
            config = new TemplateConfig();
        }

        final org.apache.velocity.app.VelocityEngine ve = new org.apache.velocity.app.VelocityEngine();

        // 编码
        final String charsetStr = config.getCharset().toString();
        ve.setProperty(Velocity.INPUT_ENCODING, charsetStr);
        ve.setProperty(Velocity.OUTPUT_ENCODING, charsetStr);
        // 使用缓存
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);

        //FILE_RESOURCE_LOADER_PATH
        ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        return ve;
    }

    @Override
    public Template getTemplate(String resource) {
        final String encoding = (String) engine.getProperty(Velocity.INPUT_ENCODING);
        return VelocityTemplate.wrap(engine.getTemplate(resource, encoding));
    }
}
