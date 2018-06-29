package com.my.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker extends Thread {

    private AtomicBoolean started = new AtomicBoolean(false);

    private Queue<Runnable> taskQueue;//任务队列

    private Selector selector;//选择器

    private ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

    public Worker(){
        taskQueue = new LinkedBlockingQueue<Runnable>();
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Worker(Selector selector){
        taskQueue = new LinkedBlockingQueue<Runnable>();
        this.selector = selector;
    }

    public void start(){
        if(!started.get()){
            if(started.compareAndSet(false,true)){
                super.start();
            }
        }
    }

    public void addTask(Runnable runnable) {
        taskQueue.offer(runnable);
    }

    @Override
    public void run() {
        while(true){
            try{
                if(!started.get()){
                    break;
                }
                runTasks();
                selector.select(10);
                processSelectedKeys();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void processSelectedKeys() throws Exception{
        Set<SelectionKey> keys = selector.selectedKeys();
        for(SelectionKey key : keys){
            if(key.isReadable()){
                //通知可读
                ConnectionContext ctx = (ConnectionContext)(key.attachment());
                read(ctx);
            }else if(key.isWritable()){
                ConnectionContext ctx = (ConnectionContext)(key.attachment());
                //ctx.getEventManager().fireChannelWrite(ctx);
            }else if(key.isConnectable()){
                ConnectionContext ctx = (ConnectionContext)(key.attachment());
                SocketChannel channel = (SocketChannel) key.channel();
                if(channel.isConnectionPending()){
                    channel.finishConnect();
                }
                channel.configureBlocking(false);
                ctx.getEventManager().fireChannelActive(ctx);
                channel.register(selector,SelectionKey.OP_READ,ctx);
            }
        }
        keys.clear();
    }

    private void read(ConnectionContext ctx){
        SocketChannel channel = ctx.getSocketChannel();
        byteBuffer.clear();
        while(true){
            try{
                int r = channel.read(byteBuffer);
                if(r == 0){
                    break;
                }
                if(r == -1){
                    ctx.getEventManager().fireChannelClose(ctx);
                    break;
                }
                byteBuffer.flip();
                ctx.getEventManager().fireChannelRead(ctx,byteBuffer);
                byteBuffer.clear();

            }catch (Exception e){
                e.printStackTrace();
                ctx.getEventManager().fireChannelClose(ctx);
                break;
            }
        }

    }

    private void runTasks() throws Exception{
        Runnable runnable = null;
        while((runnable=taskQueue.poll())!=null){
           runnable.run();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public void register(final ConnectionContext ctx,final int interestOpt) {
        taskQueue.add(new Runnable() {
            @Override
            public void run() {
                try{
                    SocketChannel socketChannel = ctx.getSocketChannel();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, interestOpt,ctx);
                }catch (Exception e){
                    e.printStackTrace();
                    ctx.getEventManager().fireChannelClose(ctx);
                }
            }
        });
    }

    public void shutdownNow(){
        if(started.compareAndSet(true,false)){
            this.interrupt();
            if(selector.isOpen()){
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void connect(ConnectionContext ctx, String host, int port) throws Exception {
        SocketChannel socketChannel = ctx.getSocketChannel();
        socketChannel.connect(new InetSocketAddress(host,port));
    }
}
