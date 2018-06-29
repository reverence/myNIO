package com.my.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class CloseHandler extends EventAdapter  {

    @Override
    public void channelClose(ConnectionContext ctx) {
        SocketChannel channel = ctx.getSocketChannel();
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
