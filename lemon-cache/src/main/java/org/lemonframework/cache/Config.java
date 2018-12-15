package org.lemonframework.cache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

/**
 * 缓存配置.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class Config {

    private JedisConfig jedisConfig;

    private LettuceConfig lettuceConfig;

    public Config() {
    }

    public Config(Config oldConf) {
        if (oldConf.getJedisConfig() != null) {
            setJedisConfig(oldConf.getJedisConfig());
        }
    }

    JedisConfig getJedisConfig() {
        return jedisConfig;
    }

    void setJedisConfig(JedisConfig jedisConfig) {
        this.jedisConfig = jedisConfig;
    }

    LettuceConfig getLettuceConfig() {
        return lettuceConfig;
    }

    void setLettuceConfig(LettuceConfig lettuceConfig) {
        this.lettuceConfig = lettuceConfig;
    }

    /**
     * Read config object stored in JSON format from <code>String</code>
     *
     * @param content of config
     * @return config
     * @throws IOException error
     */
    public static Config fromJSON(String content) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromJSON(content, Config.class);
    }

    /**
     * Read config object stored in JSON format from <code>InputStream</code>
     *
     * @param inputStream object
     * @return config
     * @throws IOException error
     */
    public static Config fromJSON(InputStream inputStream) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromJSON(inputStream, Config.class);
    }

    /**
     * Read config object stored in JSON format from <code>File</code>
     *
     * @param file object
     * @param classLoader class loader
     * @return config
     * @throws IOException error
     */
    public static Config fromJSON(File file, ClassLoader classLoader) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromJSON(file, Config.class, classLoader);
    }

    /**
     * Read config object stored in JSON format from <code>File</code>
     *
     * @param file object
     * @return config
     * @throws IOException error
     */
    public static Config fromJSON(File file) throws IOException {
        return fromJSON(file, null);
    }

    /**
     * Read config object stored in JSON format from <code>URL</code>
     *
     * @param url object
     * @return config
     * @throws IOException error
     */
    public static Config fromJSON(URL url) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromJSON(url, Config.class);
    }

    /**
     * Read config object stored in JSON format from <code>Reader</code>
     *
     * @param reader object
     * @return config
     * @throws IOException error
     */
    public static Config fromJSON(Reader reader) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromJSON(reader, Config.class);
    }

    /**
     * Convert current configuration to JSON format
     *
     * @return config in json format
     * @throws IOException error
     */
    public String toJSON() throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.toJSON(this);
    }

    /**
     * Read config object stored in YAML format from <code>String</code>
     *
     * @param content of config
     * @return config
     * @throws IOException error
     */
    public static Config fromYAML(String content) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(content, Config.class);
    }

    /**
     * Read config object stored in YAML format from <code>InputStream</code>
     *
     * @param inputStream object
     * @return config
     * @throws IOException error
     */
    public static Config fromYAML(InputStream inputStream) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(inputStream, Config.class);
    }

    /**
     * Read config object stored in YAML format from <code>File</code>
     *
     * @param file object
     * @return config
     * @throws IOException error
     */
    public static Config fromYAML(File file) throws IOException {
        return fromYAML(file, null);
    }

    public static Config fromYAML(File file, ClassLoader classLoader) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(file, Config.class, classLoader);
    }

    /**
     * Read config object stored in YAML format from <code>URL</code>
     *
     * @param url object
     * @return config
     * @throws IOException error
     */
    public static Config fromYAML(URL url) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(url, Config.class);
    }

    /**
     * Read config object stored in YAML format from <code>Reader</code>
     *
     * @param reader object
     * @return config
     * @throws IOException error
     */
    public static Config fromYAML(Reader reader) throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.fromYAML(reader, Config.class);
    }

    /**
     * Convert current configuration to YAML format
     *
     * @return config in yaml format
     * @throws IOException error
     */
    public String toYAML() throws IOException {
        ConfigSupport support = new ConfigSupport();
        return support.toYAML(this);
    }

}
