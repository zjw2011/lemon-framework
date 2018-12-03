package org.lemonframework.generator.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static void generate(final GeneratorContext context) {

    }

    private static List<Map<String, Object>> getTables(final DatabseConfig context) throws SQLException {
        final String database = context.getDatabase();
        final String tablePrefix = context.getTablePrefix();
        final String jdbcUrl = context.getUrl();
        final String jdbcUsername = context.getUsername();
        final String jdbcPassword = context.getPassword();
        final String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ? AND table_name LIKE CONCAT(?, '_%')";

        if (!DbUtils.loadDriver(context.getDriver())) {
            System.out.println("加载驱动失败");
            return null;
        }
        final Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        conn.setAutoCommit(true);
        final QueryRunner queryRunner = new QueryRunner();
        final List<Map<String, Object>> result = queryRunner.query(conn, sql, new MapListHandler(), new Object[] { database, tablePrefix });

        // 查询定制前缀项目的所有表
        final List<Map<String, Object>> tables = new ArrayList<>();
        for (Map map : result) {
            System.out.println(map.get("table_name"));
            final Map<String, Object> table = new HashMap<>(2);
            table.put("table_name", map.get("table_name"));
            table.put("model_name", StringUtil.lineToHump(StringUtil.getString(map.get("table_name"))));
            tables.add(table);
        }
        DbUtils.close(conn);

        return tables;
    }

}
