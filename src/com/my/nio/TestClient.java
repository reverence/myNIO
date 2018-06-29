package com.my.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.Set;

public class TestClient {

    public static void main(String[] args) throws Exception{
        NioClient client = new NioClient();
        EventManager manager = new EventManager();
        /*
         * LengthBasedDecoder----->ClientChannelInHandler---->LengthBasedEncoder---->ClientChannelOutHandler
         *
         */

        manager.addLast(new LengthBasedDecoder());
        manager.addLast(new ClientChannelInHandler());
        manager.addLast(new LengthBasedEncoder());
        manager.addLast(new ClientChannelOutHandler());
        manager.addLast(new CloseHandler());

        manager.addLast(new ClientChannelInHandler());
        client.eventManager(manager).connect("127.0.0.1",8888);

        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        if(str.equals("stop")){
            client.shutdownNow();
        }
    }
}
