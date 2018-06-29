package com.my.nio;

public abstract class ChannelOutHandler extends EventAdapter {
    @Override
    public void channelWrite(ConnectionContext ctx, Object msg) {
        invokeChannelWrite(ctx,msg);
    }

    protected abstract void invokeChannelWrite(ConnectionContext ctx, Object msg);
}
