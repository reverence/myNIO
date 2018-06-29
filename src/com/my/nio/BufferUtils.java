package com.my.nio;

import java.nio.ByteBuffer;

public class BufferUtils {

    /**
     * buffer须处于读模式
     * @param buffer
     * @return
     */
    public static byte[] readBytes(ByteBuffer buffer){
        byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return b;
    }

    /**
     * buffer须处于读模式
     * @param buffer
     * @return
     */
    public static String readString(ByteBuffer buffer){
        byte[] b = readBytes(buffer);
        return new String(b);
    }

    /**
     *
     * @param bb1
     * @param bb2
     * @return 返回的buffer一定是处于读模式供直接使用
     */
    public static ByteBuffer mergeByteBuffer(ByteBuffer bb1,ByteBuffer bb2){
        byte[] b1 = new byte[bb1.remaining()];
        byte[] b2 = new byte[bb2.remaining()];
        bb1.get(b1);
        bb2.get(b2);
        ByteBuffer byteBuffer = ByteBuffer.allocate(b1.length+b2.length);
        byteBuffer.put(b1);
        byteBuffer.put(b2);
        byteBuffer.flip();
        return byteBuffer;
    }
}
