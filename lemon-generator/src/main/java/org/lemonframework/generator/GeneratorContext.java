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

    private DatabaseConfig databaseConfig;

    private ProjectConfig projectConfig;

    private GeneratorConfig generatorConfig;

    private String modulePath;

    private String modelPath;

    private String mapperPath;

    private String xmlMapperPath;

    private String modelPackage;

    private String mapperPackage;

    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
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

    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getXmlMapperPath() {
        return xmlMapperPath;
    }

    public void setXmlMapperPath(String xmlMapperPath) {
        this.xmlMapperPath = xmlMapperPath;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }
}
