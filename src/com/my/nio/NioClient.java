package com.my.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class NioClient {
    private SocketChannel socketChannel;
    private Selector selector;
    private Worker worker;
    private EventManager manager;

    public NioClient(){
        try{
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            worker = new Worker(selector);
        }catch (Exception e){
            if(null != socketChannel){
                try {
                    socketChannel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        }

    }

    public NioClient eventManager(EventManager manager){
        this.manager = manager;
        return this;
    }

    public void connect(final String host, final int port){
        final ConnectionContext ctx = new ConnectionContext(worker,socketChannel,manager);
        worker.addTask(new Runnable() {
            @Override
            public void run() {
                try{
                    worker.connect(ctx,host,port);
                    worker.register(ctx,SelectionKey.OP_CONNECT);
                }catch (Exception e){
                    e.printStackTrace();
                    ctx.getEventManager().fireChannelClose(ctx);
                }

            }
        });
        worker.start();
    }

    public void shutdownNow() {
        worker.shutdownNow();
        if(selector.isOpen()){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(socketChannel.isOpen()){
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
