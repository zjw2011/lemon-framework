package org.lemonframework.generator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 代码自动生成.
 * 1 实现version 替换
 * 2 实现序列号 位于第一行,并加上注释
 *
 * @author jiawei zhang
 * 2018/11/13 4:26 PM
 */
public class LemonGenerator {

    private DatabseConfig databseConfig;

    private ProjectConfig projectConfig;

    public LemonGenerator(DatabseConfig databseConfig, ProjectConfig projectConfig) {
        this.databseConfig = databseConfig;
        this.projectConfig = projectConfig;
    }

    public GeneratorContext buildGeneratorContext() {
        final GeneratorContext context = new GeneratorContext();
        return context;
    }

    public void run() {
        if (!ObjectUtils.allNotNull(databseConfig, projectConfig)) {
            System.out.println("DatabseConfig And ProjectConfig all Not Null!!!");
            return;
        }

        if (!this.checkDatabaseConfig() ||
                !this.checkProjectConfig()) {
            return;
        }

        final GeneratorContext context = initContext();
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
        return true;
    }

    private GeneratorContext initContext() {
        final GeneratorContext context = new GeneratorContext();
        context.setDatabseConfig(databseConfig);
        context.setProjectConfig(projectConfig);
        return context;
    }

}
