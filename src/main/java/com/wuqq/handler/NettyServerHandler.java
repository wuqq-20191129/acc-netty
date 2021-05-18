package com.wuqq.handler;


import com.wuqq.base.ConstructMessageBase;
import com.wuqq.exception.MessageException;
import com.wuqq.mq.RocketMqSender;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Classname NettyServerHandler
 * @Description TODO
 * @Date 2021/1/6 9:11
 * @Created by mh
 */
//@Component
//@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //public  int temp=0;

    public static ThreadLocal<AtomicInteger> seriaNo = new ThreadLocal<AtomicInteger>(){
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }
    };
    private static Logger logger = Logger.getLogger(NettyServerHandler.class);

    private static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<String, Channel>();

    //public  static int serialNo = 0;

    private RocketMqSender sender;

    public NettyServerHandler(RocketMqSender sender) {
        this.sender =sender;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("处理消息：IP为 "+((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
        //ByteBuf byteBuf = (ByteBuf) msg;
        if(msg instanceof byte[]){
            logger.info("解码成功......");
            logger.info(Arrays.toString((byte[]) msg));

            //serialNo++;
            //if(serialNo>255){
            //    serialNo=0;
            //}
            seriaNo.get().getAndIncrement();
            if(seriaNo.get().get()>255){
                //
                seriaNo.get().set(0);
            }
            byte[] newMsg = {(byte)0xeb,0x01,(byte)seriaNo.get().get(),0,0,0x03};
            ctx.writeAndFlush(newMsg);
        }
        //ConstructMessageBase cmb = new ConstructMessageBase();
        //byte[] data = cmb.constructQuery(seriaNo);
        //logger.info("待发送消息====>"+ Arrays.toString(data));
        //ctx.writeAndFlush(data);
        //switch (dataType){
        //    case  2:
        //        //消息处理
        //        //hanleForMessage(byteBuf);
        //        break;
        //    case 3:
        //        //继续发送查询消息包
        //        //cmb.constructQuery(seriaNo);
        //        //ctx.writeAndFlush( cmb.constructQuery(seriaNo));
        //
        //}
        //byte[] data = new byte[byteBuf.readableBytes()];
        //byteBuf.getBytes(0,data);
        //
        //String messageId =""+(char)data[0]+(char)data[1];
        //logger.info("messageId"+messageId);
        //sender.send(data);
        ////super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("======>  channelReadComplet");
        //super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.warn(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String clientIp =((InetSocketAddress)(ctx.channel().remoteAddress())).getAddress().getHostAddress();
        logger.info("接收新连接 !    IP为 "+clientIp);
        channelMap.put(clientIp,ctx.channel());
        logger.info("=====> 构建查询消息包===> 序列号为0");
        //if(seriaNo!=0){
        //    logger.info("重置前的seriaNo==="+seriaNo);
        //    seriaNo=0;
        //}
        AtomicInteger serialNo = seriaNo.get();
        if(serialNo.get()!=0){
            logger.info("重置前的seriaNo==="+serialNo);
        }
        ConstructMessageBase constructMessageBase = new ConstructMessageBase();
        channelMap.get(clientIp).writeAndFlush(constructMessageBase.constructQuery(serialNo.get()));
    }
}
