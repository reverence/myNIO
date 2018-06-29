package com.my.nio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息字节长度(int)+消息内容
 */
public class LengthBasedDecoder extends ChannelInHandler {

    private List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();

    private ByteBuffer lastRemaining;

    @Override
    protected void invokeChannelRead(ConnectionContext ctx, Object msg) {
        if(msg instanceof ByteBuffer){
            buffers.clear();
            ByteBuffer byteBuffer = (ByteBuffer)msg;
            if(null != lastRemaining && lastRemaining.remaining()>0){
                //上次还有未处理的
                ByteBuffer merged = BufferUtils.mergeByteBuffer(lastRemaining,byteBuffer);
                lastRemaining.clear();
                byteBuffer.clear();
                parseBuffers(merged);
            }else {
                //上次没有未处理的
                parseBuffers(byteBuffer);
            }
            for(ByteBuffer byteBuffer1 : buffers){
                fireChannelRead(ctx,byteBuffer1);
            }
        }else{
            fireChannelRead(ctx,msg);
        }
    }

    @Override
    protected void invokeChannelActive(ConnectionContext ctx) {
        fireChannelActive(ctx);
    }

    private void parseBuffers(ByteBuffer byteBuffer){
        while(true){
            if(byteBuffer.remaining() == 0){
                break;
            }
            int len = byteBuffer.getInt();//消息长度
            if(byteBuffer.remaining()>=len){
                byte[] bytes = new byte[len];
                byteBuffer.get(bytes);
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                buffers.add(buffer);
            }else{
                ByteBuffer byteBuffer1 = ByteBuffer.allocate(len+byteBuffer.remaining());
                byteBuffer1.putInt(len);
                byteBuffer1.put(byteBuffer);
                byteBuffer1.flip();
                lastRemaining = byteBuffer1;//供下次直接读
                break;
            }
        }
    }

}
