package org.lemonframework.generator;

/**
 * 项目相关的配置.
 *
 * @author jiawei zhang
 * 2018/11/30 10:10 AM
 */
public class ProjectConfig {

    /**
     * 项目的根目录.
     */
    private String root;

    /**
     * 模块名.
     */
    private String moduleName;

    /**
     * 包名.
     */
    private String packageName;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
