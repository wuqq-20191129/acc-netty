//package com.wuqq.handler;
//
//import com.wuqq.constant.message.Message;
//import com.wuqq.exception.MessageException;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.ChannelHandler;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInboundHandlerAdapter;
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Component;
//
//import java.net.InetSocketAddress;
//
///**
// * @Classname NettyMessageFilter
// * @Description TODO
// * @Date 2021/1/13 17:12
// * @Created by mh
// */
//@Component
//@ChannelHandler.Sharable
//public class NettyMessageFilter extends ChannelInboundHandlerAdapter {
//
//    private  final Logger logger = Logger.getLogger(NettyMessageFilter.class);
//
//
//
//    public static final byte STX_B = (byte) 0xEB;
//    public static final byte ETX = 0x03;
//    public static final byte QRY = 0x01;
//    public static final byte DTA = 0x03;
//    private final byte NDT = 0x02;
//
//    private String resultCode = "";
//
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        logger.info("开始接受新连接，消息过滤：IP为 "+((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
//        super.channelActive(ctx);
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//
//        logger.info("channelRead+");
//
//
//        Message message = (Message) msg;
//        if(message.getBegin() != STX_B){
//            logger.info(" - Message start flag error!");
//            resultCode = "1101";
//            throw new MessageException(resultCode);
//        }
//        if(message.getDataTyeo() !=QRY &&message.getDataTyeo()!=DTA && message.getDataTyeo() !=NDT){
//            logger.info(" - Message type error!");
//            resultCode = "1102";
//            throw new MessageException(resultCode);
//        }
//        if (message.getSerialNo() ==-1 ) {
//            logger.info(" - Message serial NO error!");
//            resultCode = "1103";
//            throw new MessageException(resultCode);
//        }
//
//        if(message.getDataLength()!=-1){
//            if (message.getDataLength() == 0) {
//                resultCode = "0101";
//            } else if(message.getData()!=null){
//                resultCode = "0100";
//            }
//        }
//        if(message.getEnd()!=ETX){
//            logger.info(" - Message end flag error!");
//            resultCode = "1104";
//            throw new MessageException(resultCode);
//        }
//        super.channelRead(ctx, message);
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
//    }
//
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
//        logger.warn(cause.getMessage());
//        super.exceptionCaught(ctx, cause);
//    }
//}
