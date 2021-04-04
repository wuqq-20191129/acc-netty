package com.wuqq.encode;

import com.wuqq.exception.MessageException;
import com.wuqq.handler.NettyServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
//import io.netty.handler.codec.serialization.;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @Classname NettyAccDecoder
 * @Description 自定义协议 解码
 * @Date 2021/1/18 14:43
 * @Created by mh
 */
public class NettyAccDecoder extends ByteArrayDecoder {


    private static Logger logger = Logger.getLogger(NettyAccDecoder.class);


    //最小的数据长度：开头标准位1字节
    private static int MIN_DATA_LEN=6;
    //数据解码协议的开始标志
    private static byte PROTOCOL_HEADER=(byte)0xEB;
    //数据解码协议的结束标志
    private static byte PROTOCOL_TAIL=0x03;

    private final byte QRY = 0x01;
    private final byte NDT = 0x02;
    private final byte DTA = 0x03;

    private static ByteBuf buf = Unpooled.buffer();
    private static CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws MessageException, IOException, ClassNotFoundException {
        logger.info("decode=============");
        if (in.readableBytes() >= MIN_DATA_LEN) {
            logger.info("开始解码.....");
            if (buf.readableBytes() != 0) {
                in = compositeByteBuf.addComponents(true, buf, in);
                buf.clear();
            }
            in.markReaderIndex();
            byte header = in.readByte();
            if (header == PROTOCOL_HEADER) {
                logger.info("报文头正确.....");
                byte dataType = in.readByte();
                if (dataType != QRY && dataType != DTA && dataType != NDT) {
                    logger.error("非法数据类型.....");
                    return;
                }
                int serialNo = (in.readByte()) & 0xff;
                logger.info("获取到的序列号：" + serialNo);

                logger.info("自增的静态serialNo:" + NettyServerHandler.serialNo);
                if (serialNo > 255 || serialNo != NettyServerHandler.serialNo) {
                    logger.error("序列号违法.......");
                }
                int len = in.readUnsignedShort();
                if (len > in.readableBytes()) {
                    logger.error(String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际可读内容为：%2$d正在等待处理拆包……", len, in.readableBytes()));
                    in.resetReaderIndex();

                    //TCP/ip粘包，拆包？
                    return;
                } else if (len <= in.readableBytes()) {
                    byte[] data = new byte[len];
                    in.readBytes(data);
                    byte tail = in.readByte();
                    if (tail != PROTOCOL_TAIL) {
                        logger.error(String.format("数据解码协议结束标志位:%1$d [错误!]，期待的结束标志位是：%2$d", tail, PROTOCOL_TAIL));
                        return;
                    }
                    logger.info("解码成功");
                    out.add(data);
                    in.markReaderIndex();
                    this.decode(ctx, in, out);
                }
            } else {
                logger.error("报文开头不对......");
                return;
            }
        } else {
            logger.error("数据长度小于" + MIN_DATA_LEN);
            buf.clear();
            logger.info("剩余" + in.readableBytes());
            buf = in.copy(in.readerIndex(), in.readableBytes());
            logger.info("缓存区长度===" + buf.capacity());
            return;
        }
    }
}
