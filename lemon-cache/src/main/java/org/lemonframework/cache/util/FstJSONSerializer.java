package org.lemonframework.cache.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Properties;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.coders.FSTJsonFieldNames;
import org.nustaq.serialization.serializers.FSTDateSerializer;

/**
 * 使用 FST 的 JSON 对象序列化
 * 用法：
 * <p>
 * lemon.cache.serialization = json
 * json.map.list = java.util.Arrays$ArrayList
 * json.map.person = net.oschina.j2cache.demo.Person
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class FstJSONSerializer implements Serializer {

    private static final FSTConfiguration conf = FSTConfiguration.createJsonConfiguration();
    private static final String PREFIX = "map.";

    public FstJSONSerializer(Properties props) {
        conf.setJsonFieldNames(new FSTJsonFieldNames("@type", "@object", "@stype", "@seq", "@enum", "@value", "@ref"));
        conf.registerCrossPlatformClassMapping("list", "java.util.Arrays$ArrayList");
        conf.registerSerializer(Timestamp.class, new FSTDateSerializer(), true);
        conf.registerSerializer(Date.class, new FSTDateSerializer(), true);
        if (props != null) {
            props.forEach((k, v) -> {
                String key = (String) k;
                String value = (String) v;
                if (key.startsWith(PREFIX) && value != null && value.trim().length() > 0) {
                    conf.registerCrossPlatformClassMapping(key.substring(PREFIX.length()), value.trim());
                }
            });
        }
    }

    @Override
    public String name() {
        return "json";
    }

    @Override
    public byte[] serialize(Object obj) {
        return conf.asByteArray(obj);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return conf.asObject(bytes);
    }

}