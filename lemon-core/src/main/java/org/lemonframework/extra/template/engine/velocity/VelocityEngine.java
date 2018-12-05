package org.lemonframework.extra.template.engine.velocity;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.velocity.app.Velocity;
import org.lemonframework.extra.template.Engine;
import org.lemonframework.extra.template.Template;
import org.lemonframework.extra.template.TemplateConfig;

/**
 * Velocity模板引擎.
 * 
 * @author looly
 *
 */
public class VelocityEngine implements Engine {

	private org.apache.velocity.app.VelocityEngine engine;

    private String charset;

	// --------------------------------------------------------------------------------- Constructor start
	/**
	 * 默认构造
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
	 * 构造
	 * 
	 * @param engine {@link org.apache.velocity.app.VelocityEngine}
	 */
	public VelocityEngine(org.apache.velocity.app.VelocityEngine engine) {
		this.engine = engine;
        loadEncoding();
	}
	// --------------------------------------------------------------------------------- Constructor end
	
	@Override
	public Template getTemplate(String resource) {
        final org.apache.velocity.Template template = engine.getTemplate(resource, this.charset);
        template.setEncoding(this.charset);

		return VelocityTemplate.wrap(template);
	}

	/**
	 * 创建引擎
	 * 
	 * @param config 模板配置
	 * @return {@link org.apache.velocity.app.VelocityEngine}
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

		// loader
		switch (config.getResourceMode()) {
		case CLASSPATH:
			ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			break;
		case FILE:
			// path
			final String path = config.getPath();
			if (null != path) {
				ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
			}
			break;
		case WEB_ROOT:
			ve.setProperty("resource.loader", "webapp");
			ve.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
			ve.setProperty("webapp.resource.loader.path", StrUtil.nullToDefault(config.getPath(), StrUtil.SLASH));
			break;
		case STRING:
			ve.setProperty("resource.loader", "string");
			ve.setProperty("string.resource.loader.class", StringResourceLoader.class.getName());
			break;
		default:
			break;
		}

		return ve;
	}

    /**
     * 加载可用的Velocity中预定义的编码
     */
    private void loadEncoding() {
        String charset = (String) engine.getProperty(Velocity.OUTPUT_ENCODING);
        if(StrUtil.isEmpty(charset)) {
            charset = (String) engine.getProperty(Velocity.INPUT_ENCODING);
        }
        this.charset = StrUtil.isEmpty(charset) ? CharsetUtil.UTF_8 : charset;
    }
}
