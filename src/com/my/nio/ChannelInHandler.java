package com.my.nio;

public abstract class ChannelInHandler extends EventAdapter {

    @Override
    public void channelActive(ConnectionContext ctx) {
        invokeChannelActive(ctx);
    }

    @Override
    public void channelRead(ConnectionContext ctx, Object msg) {
        invokeChannelRead(ctx,msg);
    }

    protected abstract void invokeChannelRead(ConnectionContext ctx, Object msg);

    protected abstract void invokeChannelActive(ConnectionContext ctx);

}
