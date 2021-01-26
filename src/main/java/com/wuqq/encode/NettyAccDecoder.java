package com.wuqq.encode;

import com.wuqq.constant.message.Message;
import com.wuqq.exception.MessageException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
//import io.netty.handler.codec.serialization.;
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
public class NettyAccDecoder extends ByteToMessageDecoder {


    private static Logger logger = Logger.getLogger(NettyAccDecoder.class);

    private int serialNoShouldBe;
    private int serialNo;
    private boolean stopReader = false;
    private String resultCode = "";
    private int fromClient = -1;
    private final byte STX_B = (byte) 0xEB;
    private final byte ETX = 0x03;
    private final byte SERO = 0;
    private final byte QRY = 0x01;
    private final byte NDT = 0x02;
    private final byte DTA = 0x03;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws MessageException, IOException, ClassNotFoundException {
        logger.info("decode=============");



        //final int length = in.readableBytes();
        //final byte[] b = new byte[length];
        //in.getBytes(in.readerIndex(), b, 0, length);
        //
        //ByteArrayInputStream bis = new ByteArrayInputStream(b);
        //ObjectInputStream ois = new ObjectInputStream(bis);
        //out.add(ois.readObject());
        //ois.close();
    //最短消息长度6
        if(in.readableBytes()<6){
            logger.warn("Message Read Error!");
            resultCode = "1002";
            throw new MessageException(resultCode);
        }
        readOneByte(in);
        // logger.info(" - Got from client:"+fromClient);
        if ((byte) fromClient == -1) {
            logger.info(" - Message read error!");
            resultCode = "1002";
            throw new MessageException(resultCode);
        }
        if ((byte) fromClient != STX_B) {
            logger.info(" - Message start flag error!");
            resultCode = "1101";
            throw new MessageException(resultCode);
        }

        // read message type
        readOneByte(in);
        // logger.info(" - Got from client:"+fromClient);
        if (fromClient != NDT && fromClient != DTA && fromClient != QRY) {
            logger.info(" - Message type error!");
            resultCode = "1102";
            throw new MessageException(resultCode);
        }
        // read serial NO  serial No 如何轮询
        readOneByte(in);
        // logger.info(" - Got from client:"+fromClient);
        if (serialNoShouldBe != -1) { // for client test,serialNoShouldBe =
            // -1 means ignore serial no check
            if (fromClient != serialNoShouldBe) {
                logger.info(" - Message serial NO error!");
                resultCode = "1103";
                throw new MessageException(resultCode);
            }
        }
        serialNo = fromClient;

        // read data length
        readOneByte(in);
        int dataLength = fromClient + in.readByte() * 256;
        // logger.info(" - Got from client:"+dataLength);

        //data 校验？
        ByteBuf data = in.readBytes(dataLength);
        // read ETX
        readOneByte(in);
        if (fromClient != ETX) {
            logger.info(" - Message end flag error!");
            resultCode = "1104";
            throw new MessageException(resultCode);
        } else {
            // logger.info(" - Got from client:"+fromClient);
            if (dataLength == 0) {
                resultCode = "0101";
            } else {
                resultCode = "0100";
            }
        }

        out.add(data);
    }

    private void readOneByte(ByteBuf in ) throws IOException, MessageException {
        try {
            fromClient = in.readByte();
        } catch (Exception e) {
            resultCode = "1105";
            throw new MessageException(resultCode);
        }
    }
}
