package org.lemonframework.generator;

/**
 * 生成代码相关的配置.
 *
 * @author jiawei zhang
 * 2018/11/30 10:11 AM
 */
public class GeneratorConfig {

    /**
     *  生成代码的项目的根路径.
     */
    private String root;

    /**
     * 具体的相对路径.
     */
    private String relativePath = "src/main/resources/";

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
