package com.my.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOptions;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioServer extends Thread {

    private int port = 8888;//default 8888
    private int worker_size = 16;//default 16

    private AtomicBoolean stopped = new AtomicBoolean(false);

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private Worker[] workers;

    private int worker_index = 0;

    private EventManager listenerManager;

    public NioServer port(int port){
        this.port = port;
        return this;
    }

    public NioServer bind(InetSocketAddress address) throws Exception{
        serverSocketChannel.socket().bind(address);
        return this;
    }

    public NioServer bind() throws Exception{
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        return this;
    }

    public NioServer listenerManager(EventManager manager){
        this.listenerManager = manager;
        return this;
    }

    public NioServer(){
        try{
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//注册感兴趣事件
            workers = new Worker[worker_size];
            for(int i=0;i<workers.length;i++){
                workers[i] = new Worker();
            }
        }catch (Exception e){
            try{
                if(null != selector){
                    selector.close();
                }
                if(null != serverSocketChannel){
                    serverSocketChannel.close();
                }
            }catch (Exception ex){
                //ignore
            }

            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                if(stopped.get()){
                    break;
                }
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                for(SelectionKey key : keys){
                    if(key.isAcceptable()){
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        Worker worker = workers[worker_index];
                        worker_index = (worker_index+1)%workers.length;
                        ConnectionContext ctx = new ConnectionContext(worker,socketChannel,listenerManager);
                        worker.register(ctx,SelectionKey.OP_READ);
                        worker.start();
                    }
                }
                keys.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdownNow(){
        if(stopped.compareAndSet(false,true)){
            for(int i=0;i<workers.length;i++){
                Worker worker = workers[i];
                if(null != worker){
                    worker.shutdownNow();
                }
            }
            if(selector.isOpen()){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(serverSocketChannel.isOpen()){
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
