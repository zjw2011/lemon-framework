package org.lemonframework.old.cache.lettuce;

import java.nio.ByteBuffer;

import io.lettuce.core.codec.RedisCodec;

/**
 * 使用字节编码
 * @author jiawei zhang
 * @since 0.0.1
 */
public class LettuceByteCodec implements RedisCodec<String, byte[]> {

    private static final byte[] EMPTY = new byte[0];

    @Override
    public String decodeKey(ByteBuffer byteBuffer) {
        return new String(getBytes(byteBuffer));
    }

    @Override
    public byte[] decodeValue(ByteBuffer byteBuffer) {
        return getBytes(byteBuffer);
    }

    @Override
    public ByteBuffer encodeKey(String s) {
        return ByteBuffer.wrap(s.getBytes());
    }

    @Override
    public ByteBuffer encodeValue(byte[] bytes) {
        return ByteBuffer.wrap(bytes);
    }


    private static byte[] getBytes(ByteBuffer buffer) {
        int remaining = buffer.remaining();

        if (remaining == 0) {
            return EMPTY;
        }

        byte[] b = new byte[remaining];
        buffer.get(b);
        return b;
    }
}
