package com.wuqq.handler;

import com.wuqq.dao.st.StLccIp;
import com.wuqq.encode.NettyAccDecoder;
import com.wuqq.mq.RocketMqSender;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Classname NettyChannelInitializer
 * @Description 初始化handler
 * @Date 2021/1/27 10:19
 * @Created by mh
 */
@Component
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private RocketMqSender sender;

    @Autowired
    private NettyIpFilter nettyIpFilter;

    //public NettyChannelInitializer(RocketMqSender rocketMqSender,NettyIpFilter nettyIpFilter){
    //    this.sender =rocketMqSender;
    //    this.nettyIpFilter=nettyIpFilter;
    //}
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast(nettyIpFilter);
        pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        //pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,2,2,2,0));
        pipeline.addLast(new NettyAccDecoder());
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(new NettyServerHandler(sender));
    }
}
