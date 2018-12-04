package org.lemonframework.extra.template.engine.velocity;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import cn.hutool.core.io.IoUtil;
import org.apache.velocity.VelocityContext;
import org.lemonframework.extra.template.AbstractTemplate;

/**
 * Velocity模板包装
 * 
 * @author looly
 *
 */
public class VelocityTemplate extends AbstractTemplate implements Serializable {
	private static final long serialVersionUID = -132774960373894911L;

	private org.apache.velocity.Template rawTemplate;

    private Charset charset;

	/**
	 * 包装Velocity模板
	 * 
	 * @param template Velocity的模板对象 {@link org.apache.velocity.Template}
	 * @return {@link VelocityTemplate}
	 */
	public static VelocityTemplate wrap(org.apache.velocity.Template template) {
		return (null == template) ? null : new VelocityTemplate(template);
	}
	
	/**
	 * 构造
	 * 
	 * @param rawTemplate Velocity模板对象
	 */
	public VelocityTemplate(org.apache.velocity.Template rawTemplate) {
		this.rawTemplate = rawTemplate;
	}

	@Override
	public void render(Map<String, Object> bindingMap, Writer writer) {
		rawTemplate.merge(toContext(bindingMap), writer);
		IoUtil.close(writer);
	}

	@Override
	public void render(Map<String, Object> bindingMap, OutputStream out) {
	    render(bindingMap, IoUtil.getWriter(out, rawTemplate.getEncoding()));
	}

	/**
	 * 将Map转为VelocityContext
	 * 
	 * @param bindingMap 参数绑定的Map
	 * @return {@link VelocityContext}
	 */
	private VelocityContext toContext(Map<String, Object> bindingMap) {
		return new VelocityContext(bindingMap);
	}

}
