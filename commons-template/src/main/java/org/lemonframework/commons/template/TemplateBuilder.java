package org.lemonframework.commons.template;

/**
 * 模板builder.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public final class TemplateBuilder {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String DEFAULT_PATH = "/";

    private String charset;

    private String path;

    private String template;

    private TemplateBuilder() {
        this.charset = DEFAULT_CHARSET;
        this.path = DEFAULT_PATH;
    }

    public static TemplateBuilder newBuilder() {
        return new TemplateBuilder();
    }

    public TemplateBuilder charset(String charset) {
        this.charset = charset;
        return this;
    }

    public TemplateBuilder path(String path) {
        this.path = path;
        return this;
    }

    public TemplateBuilder template(String template) {
        this.template = template;
        return this;
    }

    public Template build() {
        return this.build(ResourceLoader.CLASSPATH);
    }

    public Template build(ResourceLoader loader) {
        if (this.template == null) {
            return null;
        }
        try {
            return new VelocityTemplate(this, loader);
        } catch (NoClassDefFoundError e) {
            // ignore
        }
        return null;
    }

    public String getCharset() {
        return charset;
    }

    public String getPath() {
        return path;
    }

    public String getTemplate() {
        return template;
    }

}

