package org.lemonframework.generator;

/**
 * 生成代码的上下文.
 * 1 数据库相关的
 *  1) 驱动
 *  2) URL
 *  3) 用户名
 *  4）密码
 *  5）数据库名称
 * @author jiawei zhang
 * 2018/11/12 1:53 PM
 */
public class GeneratorContext {

    private DatabseConfig databseConfig;

    private ProjectConfig projectConfig;

    private GeneratorConfig generatorConfig;

    public DatabseConfig getDatabseConfig() {
        return databseConfig;
    }

    public void setDatabseConfig(DatabseConfig databseConfig) {
        this.databseConfig = databseConfig;
    }

    public ProjectConfig getProjectConfig() {
        return projectConfig;
    }

    public void setProjectConfig(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    public GeneratorConfig getGeneratorConfig() {
        return generatorConfig;
    }

    public void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }
}
