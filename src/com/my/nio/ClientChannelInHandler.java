package com.my.nio;

import java.nio.ByteBuffer;

public class ClientChannelInHandler extends ChannelInHandler {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @Override
    protected void invokeChannelRead(ConnectionContext ctx, Object msg) {
        if(msg instanceof ByteBuffer){
            ByteBuffer byteBuffer = (ByteBuffer)msg;
            System.out.println("receive message from server:"+BufferUtils.readString(byteBuffer));
        }else {
            fireChannelRead(ctx,msg);
        }
    }

    @Override
    protected void invokeChannelActive(ConnectionContext ctx) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        String s = "this is a client";
        byteBuffer.put(s.getBytes());
        byteBuffer.flip();
        ctx.getEventManager().fireChannelWrite(ctx,byteBuffer);
    }
}
