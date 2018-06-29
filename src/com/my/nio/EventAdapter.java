package com.my.nio;

public abstract class EventAdapter implements Listener,EventFire {

    public EventAdapter next;

    @Override
    public void channelActive(ConnectionContext ctx) {
        fireChannelActive(ctx);
    }

    @Override
    public void channelRead(ConnectionContext ctx,Object msg){
        fireChannelRead(ctx,msg);
    }

    @Override
    public void channelWrite(ConnectionContext ctx,Object msg){
        fireChannelWrite(ctx,msg);
    }

    @Override
    public void channelClose(ConnectionContext ctx){
        fireChannelClose(ctx);
    }

    @Override
    public void fireChannelWrite(ConnectionContext ctx, Object msg) {
        EventAdapter next = this.next;
        if(null!=next){
            next.channelWrite(ctx,msg);
        }
    }

    @Override
    public void fireChannelClose(ConnectionContext ctx) {
        EventAdapter next = this.next;
        if(null!=next){
            next.channelClose(ctx);
        }
    }

    @Override
    public void fireChannelRead(ConnectionContext ctx, Object msg) {
        EventAdapter next = this.next;
        if(null!=next){
            next.channelRead(ctx,msg);
        }
    }

    @Override
    public void fireChannelActive(ConnectionContext ctx) {
        EventAdapter next = this.next;
        if(null != next){
            next.channelActive(ctx);
        }
    }
}
