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
import org.lemonframework.generator.DatabaseConfig;
import org.lemonframework.generator.GeneratorConfig;
import org.lemonframework.generator.GeneratorContext;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

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
        generatorCode(context.getGeneratorConfig());
    }

    private static void createGeneratorConfigXml(final GeneratorContext context) {
        final DatabaseConfig databaseConfig = context.getDatabaseConfig();
        final GeneratorConfig generatorConfig = context.getGeneratorConfig();

        final Map<String, Object> bindingMap = new HashMap<>();
        final List<Map<String, String>> bindTables = new ArrayList<>();
        List<String> tables = null;
        try {
            tables = getTables(databaseConfig);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return;
        }
        if (tables == null) {
            System.out.println("数据库中没有表");
            return;
        }
        context.setDbTables(tables);
        for (int i = 0; i < tables.size(); i++) {
            final String tableName = tables.get(i);
            final Map<String, String> table = new HashMap<>(2);
            table.put("table_name", Objects.toString(tableName));
            table.put("model_name", tableName2ClassName(tableName));
            bindTables.add(table);
        }
        bindingMap.put("tables", bindTables);

        bindingMap.put("generator_javaModelGenerator_targetPackage",context.getModelPackage());
        bindingMap.put("generator_javaClientGenerator_targetPackage",
                context.getMapperPackage());
        bindingMap.put("generator_sqlMapGenerator_targetPackage",
                context.getMapperPackage());

        bindingMap.put("generator_javaModelGenerator_targetProject",
                context.getModelPath());
        bindingMap.put("generator_javaClientGenerator_targetProject",
                context.getMapperPath());
        bindingMap.put("generator_sqlMapGenerator_targetProject",
                context.getXmlMapperPath());

        bindingMap.put("generator_jdbc_driver", databaseConfig.getDriver());
        bindingMap.put("generator_jdbc_url", databaseConfig.getUrl());
        bindingMap.put("generator_jdbc_username", databaseConfig.getUsername());
        bindingMap.put("generator_jdbc_password", databaseConfig.getPassword());
        bindingMap.put("last_insert_id_tables", databaseConfig.getLastInsertIdTables());

        final String generatorConfigXml = generatorConfig.getRoot() + generatorConfig.getRelativePath() + "/generatorConfig.xml";
        final File outFile = new File(generatorConfigXml);

        final TemplateConfig config = new TemplateConfig("", TemplateConfig.ResourceMode.CLASSPATH);
        final Engine engine = new VelocityEngine(config);
        final Template template = engine.getTemplate("template/generatorConfig.vm");
        template.render(bindingMap, outFile);
    }

    private static List<String> getTables(final DatabaseConfig context) throws SQLException {
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
        final List<String> tables = new ArrayList<>();
        for (Map map : result) {
            tables.add(Objects.toString(map.get("table_name")));
        }
        DbUtils.close(conn);

        return tables;
    }

    private static void generatorCode(final GeneratorConfig generatorConfig) {
        try {
            final String generatorConfigXml = generatorConfig.getRoot() + generatorConfig.getRelativePath() + "/generatorConfig.xml";
            final List<String> warnings = new ArrayList<>();
            final File configFile = new File(generatorConfigXml);
            final ConfigurationParser cp = new ConfigurationParser(warnings);
            final Configuration config = cp.parseConfiguration(configFile);
            final DefaultShellCallback callback = new DefaultShellCallback(true);
            final MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

            myBatisGenerator.generate(null);
            for (String warning : warnings) {
                System.out.println(warning);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String tableName2ClassName(final String tableName) {
        return StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
    }
}
