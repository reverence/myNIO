package com.my.nio;

import java.nio.ByteBuffer;

public interface EventFire {
    public void fireChannelActive(ConnectionContext ctx);
    public void fireChannelRead(ConnectionContext ctx, Object msg);
    public void fireChannelWrite(ConnectionContext ctx, Object msg);
    public void fireChannelClose(ConnectionContext ctx);
}
