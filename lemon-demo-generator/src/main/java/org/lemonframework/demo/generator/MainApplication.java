package org.lemonframework.demo.generator;

import org.lemonframework.generator.DatabaseConfig;
import org.lemonframework.generator.LemonGenerator;
import org.lemonframework.generator.ProjectConfig;
import org.lemonframework.generator.VersionHandler;

/**
 * 主入口.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class MainApplication {

    public static void main(String[] args) {
        final DatabaseConfig databseConfig = new DatabaseConfig();
        databseConfig.setUrl("jdbc:mysql://127.0.0.1:3306/ant?useUnicode=true&characterEncoding=utf-8&autoReconnect=true");
        databseConfig.setUsername("root");
        databseConfig.setPassword("123456");
        databseConfig.setDatabase("ant");
        final ProjectConfig projectConfig = new ProjectConfig();
        projectConfig.setModuleName("lemon-generator-sample");
        projectConfig.setPackageName("org.lemonframework.generator.sample");

        final LemonGenerator generator = new LemonGenerator(databseConfig, projectConfig);
        generator.registerHandler(new VersionHandler());
        generator.setClear(false);
        generator.run();
    }

}
