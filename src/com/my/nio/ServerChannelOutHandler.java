package com.my.nio;

import java.nio.ByteBuffer;

public class ServerChannelOutHandler extends ChannelOutHandler {
    @Override
    protected void invokeChannelWrite(ConnectionContext ctx, Object msg) {
        if(msg instanceof ByteBuffer){
            ctx.writeMsg((ByteBuffer)msg);
        }else{
            fireChannelWrite(ctx,msg);
        }

    }
}
