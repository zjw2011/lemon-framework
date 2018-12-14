package org.lemonframework.cache.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * 使用 Kryo 实现序列化
 *
 * @author jiawei zhang
 * @since 0.0.1
 */
public class KryoSerializer implements Serializer {

    @Override
	public String name() {
		return "kryo";
	}

	@Override
	public byte[] serialize(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (Output output = new Output(baos);){
			new Kryo().writeClassAndObject(output, obj);
			output.flush();
			return baos.toByteArray();
		}
	}

	@Override
	public Object deserialize(byte[] bits) {
		if(bits == null || bits.length == 0) {
            return null;
        }
		try (Input ois = new Input(new ByteArrayInputStream(bits))){
			return new Kryo().readClassAndObject(ois);
		}
	}
	
}
