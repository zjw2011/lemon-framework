package org.lemonframework.generator;

/**
 * 项目相关的配置.
 *
 * @author jiawei zhang
 * 2018/11/30 10:10 AM
 */
public class ProjectConfig {

    /**
     * 目标项目的根目录，可为空，默认是当前项目的上一级目录.
     */
    private String root;

    /**
     * 目标项目的模块名，root + moduleName = 目标项目的全路径.
     */
    private String moduleName;

    /**
     * 生成项目模块名.
     */
    private String generatorModuleName = "*-generator";

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

    public String getGeneratorModuleName() {
        return generatorModuleName;
    }

    public void setGeneratorModuleName(String generatorModuleName) {
        this.generatorModuleName = generatorModuleName;
    }
}
