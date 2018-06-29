package com.my.nio;

import java.util.ArrayList;
import java.util.List;

public class EventManager implements EventFire {
    private EventAdapter head;
    private EventAdapter tail;

    @Override
    public void fireChannelActive(ConnectionContext ctx) {
        head.channelActive(ctx);
    }

    @Override
    public void fireChannelRead(ConnectionContext ctx, Object msg) {
        head.channelRead(ctx,msg);
    }

    @Override
    public void fireChannelWrite(ConnectionContext ctx, Object msg) {
        head.channelWrite(ctx,msg);
    }

    @Override
    public void fireChannelClose(ConnectionContext ctx) {
        head.channelClose(ctx);
    }

    public synchronized void addLast(EventAdapter adapter){
        if(head == null){
            head = adapter;
            tail = adapter;
        }else{
            tail.next = adapter;
            tail = adapter;
        }
    }
}
