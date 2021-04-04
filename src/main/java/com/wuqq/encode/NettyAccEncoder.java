package com.wuqq.encode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @Classname NettyAccEncoder
 * @Description 预留  传输实体类POJO
 * @Date 2021/1/18 14:57
 * @Created by mh
 */
public class NettyAccEncoder extends ByteArrayEncoder {

    private static Logger logger = Logger.getLogger(NettyAccEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
