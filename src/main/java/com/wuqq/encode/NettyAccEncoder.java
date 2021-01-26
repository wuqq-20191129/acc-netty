package com.wuqq.encode;

import com.wuqq.constant.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;

/**
 * @Classname NettyAccEncoder
 * @Description TODO
 * @Date 2021/1/18 14:57
 * @Created by mh
 */
public class NettyAccEncoder extends MessageToByteEncoder {

    private static Logger logger = Logger.getLogger(NettyAccEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        //logger.info("adasdfadadfasdfadsfasdfad");
        Message message =(Message) o;
        logger.info("message==="+message.toString());

    }
}
