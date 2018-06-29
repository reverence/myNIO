package com.my.nio;

import java.util.Scanner;

public class TestServer {
    public static void main(String[] args) throws Exception{
        EventManager eventManager = new EventManager();
        /**
         * LengthBasedDecoder------>ServerChannelInHandler---->LengthBasedEncoder---->ServerChannelOutHandler
         */
        eventManager.addLast(new LengthBasedDecoder());
        eventManager.addLast(new ServerChannelInHandler());
        eventManager.addLast(new LengthBasedEncoder());
        eventManager.addLast(new ServerChannelOutHandler());
        eventManager.addLast(new CloseHandler());
        NioServer server = new NioServer().listenerManager(eventManager).bind();
        server.start();

        Scanner s = new Scanner(System.in);
        String str = s.nextLine();
        if(str.equals("stop")){
            server.shutdownNow();
        }
    }
}
