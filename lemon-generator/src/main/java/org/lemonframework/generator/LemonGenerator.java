package org.lemonframework.generator;

import java.io.File;

import cn.hutool.core.io.resource.ResourceUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.lemonframework.generator.util.GeneratorUtil;

/**
 * 代码自动生成.
 * 1 实现version 替换
 * 2 实现序列号 位于第一行,并加上注释
 *
 * @author jiawei zhang
 * 2018/11/13 4:26 PM
 */
public class LemonGenerator {

    private DatabaseConfig databseConfig;

    private ProjectConfig projectConfig;

    public LemonGenerator(DatabaseConfig databseConfig, ProjectConfig projectConfig) {
        this.databseConfig = databseConfig;
        this.projectConfig = projectConfig;
    }

    public GeneratorContext buildGeneratorContext() {
        final GeneratorContext context = new GeneratorContext();
        return context;
    }

    public void run() {
        if (!ObjectUtils.allNotNull(databseConfig, projectConfig)) {
            System.out.println("DatabaseConfig And ProjectConfig all Not Null!!!");
            return;
        }

        if (!this.checkDatabaseConfig() ||
                !this.checkProjectConfig()) {
            return;
        }

        final GeneratorContext context = initContext();
        if (context == null) {
           return;
        }

        GeneratorUtil.generate(context);
    }

    private boolean checkDatabaseConfig() {
        final String url = databseConfig.getUrl();
        if (StringUtils.startsWith(url, "jdbc:mysql:")) {
            databseConfig.setDriver("com.mysql.jdbc.Driver");
        } else {
            System.out.println("Support database[Mysql]!");
            return false;
        }

        return true;
    }

    private boolean checkProjectConfig() {
        if (StringUtils.isBlank(projectConfig.getModuleName())) {
            System.out.println("Please set moudleName!");
            return false;
        }

        if (StringUtils.isBlank(projectConfig.getPackageName())) {
            System.out.println("Please set packageName!");
            return false;
        }

        return true;
    }

    private boolean initRootPath(final GeneratorContext context) {
        final String path = ResourceUtil.getResource("").getPath();
        final int idx = path.lastIndexOf("-generator/");
        if (idx > 0) {
            //表示和当前生成代码的项目是在同一个空间下
            if (StringUtils.isBlank(context.getProjectConfig().getRoot())) {
                final String subPath = path.substring(0, idx);
                final String rootPath = subPath.substring(0, subPath.lastIndexOf("/") + 1);
                context.getProjectConfig().setRoot(rootPath);
            }
            final String generatorRootPath = path.substring(0, idx + "-generator/".length());

            context.getGeneratorConfig().setRoot(generatorRootPath);
            return true;
        }

        return false;
    }

    private void cleanOldFiles(final GeneratorContext context) {
        try {
            //删除model
            FileUtils.deleteDirectory(new File(context.getModelPath()));
            //删除mapper
            FileUtils.deleteDirectory(new File(context.getMapperPath()));
            //删除xml
            FileUtils.deleteDirectory(new File(context.getXmlMapperPath()));
        } catch (Exception e) {
            // do nothing
        }
    }

    private GeneratorContext initContext() {
        final GeneratorContext context = new GeneratorContext();
        final GeneratorConfig generatorConfig = new GeneratorConfig();
        context.setDatabaseConfig(databseConfig);
        context.setProjectConfig(projectConfig);
        context.setGeneratorConfig(generatorConfig);
        if (!initRootPath(context)) {
            System.out.println("初始化根目录失败");
            return null;
        }

        final String modelPackage = projectConfig.getPackageName() + ".dao.model";
        final String mapperPackage = projectConfig.getPackageName() + ".dao.mapper";

        final String modelPath = modelPackage.replace("\\.", "/");
        final String mapperPath = mapperPackage.replace("\\.", "/");
        final String modulePath = projectConfig.getRoot() + projectConfig.getModuleName();

        context.setModelPackage(modelPackage);
        context.setMapperPackage(mapperPackage);
        context.setModulePath(modulePath);
        context.setModelPath(modulePath + "/src/main/java/" + modelPath);
        context.setMapperPath(modulePath + "/src/main/java/" + mapperPath);
        context.setXmlMapperPath(modulePath + "/src/main/resources/" + mapperPath);

        cleanOldFiles(context);

//        FileUtils.forceMkdir(new File(generatorConfigXmlDir));

        return context;
    }

    public static void main(String[] args) {
        final String path = ResourceUtil.getResource("").getPath();
        final int idx = path.lastIndexOf("-generator/");
        if (idx > 0) {
            //表示和当前生成代码的项目是在同一个空间下
                final String subPath = path.substring(0, idx);
                final String rootPath = subPath.substring(0, subPath.lastIndexOf("/") + 1);
                System.out.println(rootPath);
            final String generatorRootPath = path.substring(0, idx + "-generator/".length());

            System.out.println(generatorRootPath);
        }

    }

}
