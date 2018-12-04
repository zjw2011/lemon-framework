package org.lemonframework.extra.template2.engine.velocity;

import java.io.OutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.Map;

import cn.hutool.core.io.IoUtil;
import org.apache.velocity.VelocityContext;
import org.lemonframework.extra.template2.AbstractTemplate;

/**
 * desc.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class VelocityTemplate extends AbstractTemplate implements Serializable {

    private static final long serialVersionUID = 3168101480696364622L;

    private org.apache.velocity.Template rawTemplate;

    /**
     * 构造.
     *
     * @param rawTemplate Velocity模板对象
     */
    public VelocityTemplate(org.apache.velocity.Template rawTemplate) {
        this.rawTemplate = rawTemplate;
    }

    /**
     * 包装Velocity模板.
     *
     * @param template Velocity的模板对象 {@link org.apache.velocity.Template}
     * @return {@link VelocityTemplate}
     */
    public static VelocityTemplate wrap(org.apache.velocity.Template template) {
        return (null == template) ? null : new VelocityTemplate(template);
    }

    @Override
    public void render(Map<String, Object> bindingMap, OutputStream out) {
//        if(null == charset) {
//            loadEncoding();
//        }
        render(bindingMap, IoUtil.getWriter(out, "UTF-8"));
    }

    @Override
    public void render(Map<String, Object> bindingMap, Writer writer) {
        this.rawTemplate.merge(toContext(bindingMap), writer);
        IoUtil.close(writer);
    }

    /**
     * 将Map转为VelocityContext.
     *
     * @param bindingMap 参数绑定的Map
     * @return {@link VelocityContext}
     */
    private VelocityContext toContext(Map<String, Object> bindingMap) {
        return new VelocityContext(bindingMap);
    }
}
