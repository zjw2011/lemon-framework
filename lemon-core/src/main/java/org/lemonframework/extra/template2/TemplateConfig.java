package org.lemonframework.extra.template2;

import java.io.Serializable;
import java.nio.charset.Charset;

import cn.hutool.core.util.CharsetUtil;

/**
 * 模板配置.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class TemplateConfig implements Serializable {
    private static final long serialVersionUID = 2516893621485603564L;

    /**
     * 编码.
     */
    private Charset charset;

    /**
     * 配置文件的路径，如果ClassPath或者WebRoot模式，则表示相对路径.
     */
    private String path;

    /**
     * 模板资源加载方式.
     * */
    private ResourceMode resourceMode;

    /**
     * 默认构造，使用UTF8编码，默认从ClassPath获取模板
     */
    public TemplateConfig() {
        this(null);
    }

    /**
     * 构造，默认UTF-8编码
     *
     * @param path 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
     */
    public TemplateConfig(String path) {
        this(path, ResourceMode.STRING);
    }

    /**
     * 构造，默认UTF-8编码
     *
     * @param path 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
     * @param resourceMode 模板资源加载方式
     */
    public TemplateConfig(String path, ResourceMode resourceMode) {
        this(CharsetUtil.CHARSET_UTF_8, path, resourceMode);
    }


    /**
     * 构造
     *
     * @param charset 编码
     * @param path 模板路径，如果ClassPath或者WebRoot模式，则表示相对路径
     * @param resourceMode 模板资源加载方式
     */
    public TemplateConfig(Charset charset, String path, ResourceMode resourceMode) {
        this.charset = charset;
        this.path = path;
        this.resourceMode = resourceMode;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ResourceMode getResourceMode() {
        return resourceMode;
    }

    public void setResourceMode(ResourceMode resourceMode) {
        this.resourceMode = resourceMode;
    }

    /**
     * 资源模式.
     *
     * @author jiawei zhang
     * @since 0.0.1
     */
    public enum ResourceMode {

        /**
         * 从ClassPath加载模板.
         * */
        CLASSPATH,

        /**
         * 从File目录加载模板.
         * */
        FILE,

        /**
         * 从WebRoot目录加载模板.
         * */
        WEB_ROOT,

        /**
         * 从模板文本加载模板.
         * */
        STRING,

        /**
         * 复合加载模板（分别从File、ClassPath、Web-root、String方式尝试查找模板.
         * */
        COMPOSITE;
    }
}
