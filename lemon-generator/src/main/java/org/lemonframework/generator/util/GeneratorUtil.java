package org.lemonframework.generator.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.lemonframework.extra.template.Engine;
import org.lemonframework.extra.template.Template;
import org.lemonframework.extra.template.TemplateConfig;
import org.lemonframework.extra.template.engine.velocity.VelocityEngine;
import org.lemonframework.generator.DatabseConfig;
import org.lemonframework.generator.GeneratorContext;
import org.lemonframework.generator.ProjectConfig;

/**
 * 生成代码的util.
 *
 * @author jiawei zhang
 * 2018/12/3 10:21 AM
 */
public class GeneratorUtil {

    public static void generate(final GeneratorContext context) {
        createGeneratorConfigXml(context);
        //2 MBG生成代码
        //3 生成业务代码
        if (context.getDatabseConfig().isIncludeVersion()) {
            createVersion();
        }
    }

    private static void createGeneratorConfigXml(final GeneratorContext context) {
        final ProjectConfig projectConfig = context.getProjectConfig();
        final DatabseConfig databseConfig = context.getDatabseConfig();
        final String modulePath = projectConfig.getRoot() + projectConfig.getModuleName();
        final String packagePath = projectConfig.getPackageName().replace("\\.", "/");
        final TemplateConfig config = new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH);
        final Engine engine = new VelocityEngine(config);
        final Template template = engine.getTemplate("template/generatorConfig.vm");
        final Map<String, Object> bindingMap = new HashMap<>();
        final String projectResourcesPath = modulePath + "/src/main/resources";

        try {
            bindingMap.put("tables", getTables(databseConfig));
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return;
        }
        bindingMap.put("generator_javaModelGenerator_targetPackage",
                modulePath + "src/main/" + packagePath + "/dao/model");
        bindingMap.put("generator_sqlMapGenerator_targetPackage",
                modulePath + "src/main/" + packagePath + "/dao/mapper");
        bindingMap.put("generator_javaClientGenerator_targetPackage",
                modulePath + "src/main/" + packagePath + "/dao/mapper");

        bindingMap.put("generator_javaModelGenerator_targetProject",
                modulePath);
        bindingMap.put("generator_sqlMapGenerator_targetProject",
                modulePath);
        bindingMap.put("generator_javaClientGenerator_targetProject",
                modulePath);

        bindingMap.put("generator_jdbc_driver", databseConfig.getDriver());
        bindingMap.put("generator_jdbc_url", databseConfig.getUrl());
        bindingMap.put("generator_jdbc_username", databseConfig.getUsername());
        bindingMap.put("generator_jdbc_password", databseConfig.getPassword());
        bindingMap.put("last_insert_id_tables", databseConfig.getLastInsertIdTables());

        final String fileName = projectResourcesPath + "template/generatorConfig.xml";
        final File outFile = new File(fileName);
        template.render(bindingMap, outFile);

    }

    private static List<Map<String, String>> getTables(final DatabseConfig context) throws SQLException {
        final String database = context.getDatabase();
        final String tablePrefix = context.getTablePrefix();
        final String jdbcUrl = context.getUrl();
        final String jdbcUsername = context.getUsername();
        final String jdbcPassword = context.getPassword();
        final String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ? AND table_name LIKE CONCAT(?, '_%')";

        if (!DbUtils.loadDriver(context.getDriver())) {
            System.out.println("数据库错误:加载驱动失败");
            return null;
        }
        final Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        conn.setAutoCommit(true);
        final QueryRunner queryRunner = new QueryRunner();
        final List<Map<String, Object>> result = queryRunner.query(conn, sql, new MapListHandler(), new Object[] { database, tablePrefix });

        // 查询定制前缀项目的所有表
        final List<Map<String, String>> tables = new ArrayList<>();
        for (Map map : result) {
            final Map<String, String> table = new HashMap<>(2);
            table.put("table_name", Objects.toString(map.get("table_name")));
            table.put("model_name", StrUtil.toCamelCase(Objects.toString(map.get("table_name"), "")));
            tables.add(table);
        }
        DbUtils.close(conn);

        return tables;
    }

    public static void createVersion() {

    }

}
