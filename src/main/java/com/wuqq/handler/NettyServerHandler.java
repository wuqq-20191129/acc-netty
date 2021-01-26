package com.wuqq.handler;

//import com.wuqq.base.ConstructMessageBase;
import com.wuqq.constant.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @Classname NettyServerHandler
 * @Description TODO
 * @Date 2021/1/6 9:11
 * @Created by mh
 */
@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    public static  int seriaNo;

    private static Logger logger = Logger.getLogger(NettyServerHandler.class);



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("处理消息：IP为 "+((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
        //Message buf = (Message) msg;
        //logger.info(buf.toString());

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0,data);

        String messageId =""+(char)data[0]+(char)data[1];
        logger.info("messageId"+messageId);
        //super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelReadComplet");
        //super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.warn(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //logger.info("接收新连接 发送查询数据包：IP为 "+((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
        //while (true) {
        //    seriaNo += 1;
        //    if (seriaNo > 255) {
        //        seriaNo = 0;
        //    }
        //    //ctx.writeAndFlush(new ConstructMessageBase().constructQuery(seriaNo));
        //    ctx.channel().writeAndFlush((new ConstructMessageBase().constructQuery(seriaNo)));
        //}
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //logger.info("连接不合法！ 关闭连接~");


        super.channelInactive(ctx);
    }
}
