package org.lemonframework.cache;

/**
 * 使用Jedis的配置.
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class JedisConfig extends BaseConfig<JedisConfig> {

    JedisConfig() {
    }

    JedisConfig(JedisConfig config) {
        super(config);
    }
}
