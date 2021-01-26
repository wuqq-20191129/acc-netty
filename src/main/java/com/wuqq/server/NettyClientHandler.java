package com.wuqq.server;

import com.wuqq.constant.message.Message;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * @Classname NettyClientHanlder
 * @Description TODO
 * @Date 2021/1/19 15:53
 * @Created by mh
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(NettyClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("sdadfasdasdfas");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("wwwwwwwwwwwwwwwwwwwwww");
        Message message = (Message) msg;
        logger.info("000000000000"+message.toString());
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.warn(cause.getMessage());
        //super.exceptionCaught(ctx, cause);
    }
}
