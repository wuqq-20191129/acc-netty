package com.wuqq.test;

/**
 * @Classname TimeClientHandler
 * @Description TODO
 * @Date 2021/1/19 16:15
 * @Created by mh
 */
import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelInboundHandlerAdapter;


import java.util.Date;


public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    @Override

    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //
        //ByteBuf m = (ByteBuf) msg;
        //
        //try {
        //
        //    //long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
        //    //
        //    //System.out.println(new Date(currentTimeMillis));
        //
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        int i = 255;
        while(i>0){
            byte[] msg = {(byte)0xeb,0x01,0x00,0,0,0x03};
            msg[2] = (byte) i;
            ctx.writeAndFlush(msg);
            i--;
        }
    }

    @Override

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        cause.printStackTrace();

        ctx.close();

    }
}