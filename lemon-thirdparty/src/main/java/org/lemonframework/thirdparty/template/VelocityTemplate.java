package org.lemonframework.thirdparty.template;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

class VelocityTemplate extends AbstractTemplate implements Serializable {

    private static final long serialVersionUID = -7006349284992425929L;

    private org.apache.velocity.Template rawTemplate;

    private String charset;

    public VelocityTemplate(TemplateBuilder builder, TemplateLoader loader) {

        final org.apache.velocity.app.VelocityEngine ve = new org.apache.velocity.app.VelocityEngine();

        // 编码
        ve.setProperty(Velocity.INPUT_ENCODING, builder.getCharset());
        ve.setProperty(Velocity.OUTPUT_ENCODING, builder.getCharset());
        ve.setProperty(Velocity.ENCODING_DEFAULT, builder.getCharset());
        // 使用缓存
        ve.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, true);

        // loader
        switch (loader) {
            case CLASSPATH:
                ve.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
                break;
            case FILE:
                // path
                ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, builder.getPath());
                break;
            case WEB_ROOT:
                ve.setProperty(Velocity.RESOURCE_LOADER, "webapp");
                ve.setProperty("webapp.resource.loader.class", "org.apache.velocity.tools.view.servlet.WebappLoader");
                ve.setProperty("webapp.resource.loader.path", builder.getPath());
                break;
            case STRING:
                ve.setProperty(Velocity.RESOURCE_LOADER, "str");
                ve.setProperty("str.resource.loader.class", SimpleStringResourceLoader.class.getName());
                break;
            default:
                break;
        }

        ve.init();

        this.rawTemplate = ve.getTemplate(builder.getTemplate());
        this.charset = builder.getCharset();
    }

    /**
     * 将Map转为VelocityContext.
     *
     * @param context 参数绑定的Map
     * @return {@link VelocityContext}
     */
    private VelocityContext toContext(Map context) {
        return new VelocityContext(context);
    }

    @Override
    public void render(Map context, Writer writer) {
        this.rawTemplate.merge(toContext(context), writer);
        flush(writer);
    }

    @Override
    public void render(Map context, OutputStream out) {
        render(context, new OutputStreamWriter(out, Charset.forName(this.charset)));
    }

}