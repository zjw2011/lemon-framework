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
import cn.hutool.extra.template.Engine;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.lemonframework.generator.DatabseConfig;
import org.lemonframework.generator.GeneratorContext;

/**
 * 生成代码的util.
 *
 * @author jiawei zhang
 * 2018/12/3 10:21 AM
 */
public class GeneratorUtil {

    private static final Log logger = LogFactory.get(GeneratorUtil.class);

    public static void generate(final GeneratorContext context) {

    }

    private static void createGeneratorConfigXml(final GeneratorContext context) {

        final Map<String, Object> bindingMap = new HashMap<>();

        Engine engine = TemplateUtil.createEngine(new TemplateConfig("template/generatorConfig.vm", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("template/generatorConfig.vm");
        File file = new File("template/generatorConfig.vm1");
        template.render(bindingMap, file);
    }

    private static List<Map<String, String>> getTables(final DatabseConfig context) throws SQLException {
        final String database = context.getDatabase();
        final String tablePrefix = context.getTablePrefix();
        final String jdbcUrl = context.getUrl();
        final String jdbcUsername = context.getUsername();
        final String jdbcPassword = context.getPassword();
        final String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ? AND table_name LIKE CONCAT(?, '_%')";

        if (!DbUtils.loadDriver(context.getDriver())) {
            logger.error("数据库错误:{}", "加载驱动失败");
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

}
