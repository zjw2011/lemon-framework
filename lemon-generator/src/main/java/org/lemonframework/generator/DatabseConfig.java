package org.lemonframework.generator;

import java.util.Map;

/**
 * 数据库配置.
 *  1) 驱动
 *  2) URL
 *  3) 用户名
 *  4）密码
 *  5）数据库名称
 *
 * @author jiawei zhang
 * 2018/11/16 5:01 PM
 */
public class DatabseConfig {
    /**
     * 驱动.
     */
    private String driver = "com.mysql.jdbc.Driver";

    /**
     * url.
     */
    private String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";

    /**
     * 用户名.
     */
    private String username = "test";

    /**
     * 密码.
     */
    private String password = "";

    /**
     * 数据库名称.
     */
    private String database = "test";

    /**
     * 表前缀.
     */
    private String tablePrefix = "";

    /**
     * 行版本号.
     */
    private boolean includeVersion = false;

    /**
     * 需要生成ID的表.
     */
    private Map<String, Object> lastInsertIdTables;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public Map<String, Object> getLastInsertIdTables() {
        return lastInsertIdTables;
    }

    public void setLastInsertIdTables(Map<String, Object> lastInsertIdTables) {
        this.lastInsertIdTables = lastInsertIdTables;
    }

    public boolean isIncludeVersion() {
        return includeVersion;
    }

    public void setIncludeVersion(boolean includeVersion) {
        this.includeVersion = includeVersion;
    }
}
