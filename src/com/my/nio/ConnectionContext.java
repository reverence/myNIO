package com.my.nio;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import sun.applet.resources.MsgAppletViewer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ConnectionContext {
    private Worker worker;//工作线程
    private SocketChannel socketChannel;//通道
    private EventManager eventManager;

    public ConnectionContext(Worker w,SocketChannel channel,EventManager manager){
        this.worker = w;
        this.socketChannel = channel;
        this.eventManager = manager;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void writeMsg(ByteBuffer buffer){
        try {
            socketChannel.write(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
