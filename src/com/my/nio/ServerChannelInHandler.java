package com.my.nio;

import java.nio.ByteBuffer;

public class ServerChannelInHandler extends ChannelInHandler {

    private static final int INT_LENGHT = 4;

    @Override
    protected void invokeChannelRead(ConnectionContext ctx, Object msg) {
        if(msg instanceof ByteBuffer){
            ByteBuffer byteBuffer = (ByteBuffer)msg;
            String message = BufferUtils.readString(byteBuffer);
            System.out.println(message);
            byteBuffer.flip();
            byteBuffer.clear();
            byteBuffer.put(message.getBytes());
            byteBuffer.flip();
            fireChannelWrite(ctx,byteBuffer);
        }else{
            fireChannelRead(ctx,msg);
        }
    }

    @Override
    protected void invokeChannelActive(ConnectionContext ctx) {
            fireChannelActive(ctx);
    }
}
