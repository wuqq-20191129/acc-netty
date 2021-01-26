package com.wuqq.test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Classname TimeServerHandler
 * @Description TODO
 * @Date 2021/1/19 16:13
 * @Created by mh
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {



    //ChannelHandlerContext通道处理上下文

    @Override

    public void channelActive(final ChannelHandlerContext ctx) throws InterruptedException { // (1)


        while (true) {

            ByteBuf time = ctx.alloc().buffer(4); //为ByteBuf分配四个字节

            time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

            ctx.writeAndFlush(time); // (3)

            Thread.sleep(2000);

        }

    }


    @Override

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();

        ctx.close();

    }
}