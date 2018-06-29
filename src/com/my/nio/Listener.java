package com.my.nio;

import java.nio.ByteBuffer;

public interface Listener {//处理各种事件
    public void channelActive(ConnectionContext ctx);
    public void channelRead(ConnectionContext ctx,Object msg) ;
    public void channelWrite(ConnectionContext ctx,Object msg) ;
    public void channelClose(ConnectionContext ctx);
}
