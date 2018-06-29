package com.my.nio;

import java.nio.ByteBuffer;

public class LengthBasedEncoder extends ChannelOutHandler {

    private static final int INT_LENGTH = 4;

    @Override
    protected void invokeChannelWrite(ConnectionContext ctx, Object msg) {
        if(msg instanceof ByteBuffer){
            ByteBuffer byteBuffer = (ByteBuffer)msg;
            ByteBuffer buffer = encode(byteBuffer);
            fireChannelWrite(ctx,buffer);
        }else{
            fireChannelWrite(ctx,msg);
        }
    }

    private ByteBuffer encode(ByteBuffer byteBuffer) {
        int len = byteBuffer.remaining();
        ByteBuffer buffer = ByteBuffer.allocate(len+INT_LENGTH);
        buffer.putInt(len);
        buffer.put(byteBuffer);
        buffer.flip();
        return buffer;
    }
}
