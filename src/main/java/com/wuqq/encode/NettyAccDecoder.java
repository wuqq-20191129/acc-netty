package com.wuqq.encode;

import com.wuqq.constant.message.Message;
import com.wuqq.exception.MessageException;
import com.wuqq.handler.NettyServerHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
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


    private static int MIN_DATA_LEN =6;
    private  static byte PROTOCOL_HEADER = (byte)0xEB;
    private  static  byte  PROTOCOL_TAIL = (byte)0x03;
    private final byte QRY = 0x01;
    private final byte NDT = 0x02;
    private final byte DTA = 0x03;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws MessageException, IOException, ClassNotFoundException {
        logger.info("decode=============");
        if(in.readableBytes()>=6){
            logger.info("开始解码.....");
            in.markReaderIndex();
            byte header = in.readByte();
            if(header == PROTOCOL_HEADER) {
                logger.info("报文头正确.....");
                byte dataType = in.readByte();
                if (dataType != QRY && dataType != DTA && dataType != NDT) {
                    logger.error("非法数据类型.....");
                    return;
                }
                int serialNo = in.readByte();
                if (serialNo < 0 || serialNo > 255) {
                    logger.error("序列号违法.......");
                }
                int len = in.readUnsignedShort();
                if (len > in.readableBytes()) {
                    logger.error(String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际可读内容为：%2$d正在等待处理拆包……", len, in.readableBytes()));
                    in.resetReaderIndex();

                    //TCP/ip粘包，拆包？
                    return;
                }
                byte[] data = new byte[len];
                in.readBytes(data);
                byte tail = in.readByte();
                if (tail != PROTOCOL_TAIL) {
                    logger.error(String.format("数据解码协议结束标志位:%1$d [错误!]，期待的结束标志位是：%2$d", tail, PROTOCOL_TAIL));
                    return;
                }
                logger.info("解码成功");
                out.add(data);
            }else{
                logger.error("报文开头不对......");
                return;
            }
        }else{
            logger.error("数据长度小于"+MIN_DATA_LEN);
            return;
        }

        ReferenceCountUtil.retain(in);
        out.add(in);

    }

    //private void readOneByte(ByteBuf in ) throws IOException, MessageException {
    //    try {
    //        fromClient = in.readByte();
    //    } catch (Exception e) {
    //        resultCode = "1105";
    //        throw new MessageException(resultCode);
    //    }
    //}
}
