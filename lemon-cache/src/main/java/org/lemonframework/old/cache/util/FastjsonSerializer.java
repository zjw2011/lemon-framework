package org.lemonframework.old.cache.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 使用 fastjson 进行对象的 JSON 格式序列化
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class FastjsonSerializer implements Serializer {

    @Override
    public String name() {
        return "fastjson";
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName).getBytes();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return JSON.parse(new String(bytes), Feature.SupportAutoType);
    }

}
