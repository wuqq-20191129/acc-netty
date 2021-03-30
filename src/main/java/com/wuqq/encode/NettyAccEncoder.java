package com.wuqq.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

/**
 * @Classname NettyAccEncoder
 * @Description 预留  传输实体类POJO
 * @Date 2021/1/18 14:57
 * @Created by mh
 */
public class NettyAccEncoder extends MessageToByteEncoder {

    private static Logger logger = Logger.getLogger(NettyAccEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //logger.info("adasdfadadfasdfadsfasdfad");

    }
}
