package org.lemonframework.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.XmlUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.lemonframework.generator.util.GeneratorUtil;

/**
 * 代码自动生成.
 * 1 实现version 替换
 *
 * @author jiawei zhang
 * 2018/11/13 4:26 PM
 */
public class LemonGenerator {

    private DatabaseConfig databseConfig;

    private ProjectConfig projectConfig;

    private List<GeneratorHandler> handlers;

    private boolean clear;

    public LemonGenerator(DatabaseConfig databseConfig, ProjectConfig projectConfig) {
        this.databseConfig = databseConfig;
        this.projectConfig = projectConfig;
        this.clear = false;
        handlers = new ArrayList<>();
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

        if (this.clear) {
            return;
        }

        GeneratorUtil.generate(context);
        for (int i = 0; i < this.handlers.size(); i++) {
            this.handlers.get(i).run(context);
        }
    }

    private boolean checkDatabaseConfig() {
        final String url = databseConfig.getUrl();
        if (StringUtils.startsWith(url, "jdbc:mysql:")) {
            databseConfig.setDriver("com.mysql.jdbc.Driver");
        } else {
            System.out.println("Support database[Mysql]!");
            return false;
        }

        databseConfig.setUrl(XmlUtil.escape(url));

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
        final ProjectConfig projectConfig = context.getProjectConfig();
        final GeneratorConfig generatorConfig = context.getGeneratorConfig();
        final String generatorModulePath = projectConfig.getGeneratorModuleName() + "/";
        final String generatorNameSubfix =
                generatorModulePath.startsWith("*") ? generatorModulePath.substring(1) : generatorModulePath;

        final int idx = path.lastIndexOf(generatorNameSubfix);
        if (idx > 0) {
            //表示和当前生成代码的项目是在同一个空间下
            if (StringUtils.isBlank(projectConfig.getRoot())) {
                final String subPath = path.substring(0, idx);
                final String rootPath = subPath.substring(0, subPath.lastIndexOf("/") + 1);
                projectConfig.setRoot(rootPath);
            }
            final String generatorRootPath = path.substring(0, idx + generatorNameSubfix.length());

            generatorConfig.setRoot(generatorRootPath);
            return true;
        }

        return false;
    }

    private void cleanOldFiles(final GeneratorContext context) {
        final String modelPackage = context.getModelPackage();
        final String mapperPackage = context.getMapperPackage();

        final String modelPath = modelPackage.replaceAll("\\.", "/");
        final String mapperPath = mapperPackage.replaceAll("\\.", "/");
        try {
            //删除model
            FileUtils.deleteDirectory(new File(context.getModelPath() + "/" + modelPath));
            //删除mapper
            FileUtils.deleteDirectory(new File(context.getMapperPath() + "/" + mapperPath));
            //删除xml
            FileUtils.deleteDirectory(new File(context.getXmlMapperPath() + "/" + mapperPath));
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

        final String modulePath = projectConfig.getRoot() + projectConfig.getModuleName();

        context.setModelPackage(modelPackage);
        context.setMapperPackage(mapperPackage);
        context.setModulePath(modulePath);
        context.setModelPath(modulePath + "/src/main/java");
        context.setMapperPath(modulePath + "/src/main/java");
        context.setXmlMapperPath(modulePath + "/src/main/resources");

        cleanOldFiles(context);

//        FileUtils.forceMkdir(new File(generatorConfigXmlDir));

        return context;
    }

    public void registerHandler(GeneratorHandler handler) {
        this.handlers.add(handler);
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    //    public static void main(String[] args) {
//        final String path = ResourceUtil.getResource("").getPath();
//        final int idx = path.lastIndexOf("-generator/");
//        if (idx > 0) {
//            //表示和当前生成代码的项目是在同一个空间下
//                final String subPath = path.substring(0, idx);
//                final String rootPath = subPath.substring(0, subPath.lastIndexOf("/") + 1);
//                System.out.println(rootPath);
//            final String generatorRootPath = path.substring(0, idx + "-generator/".length());
//
//            System.out.println(generatorRootPath);
//        }
//
//    }

}
