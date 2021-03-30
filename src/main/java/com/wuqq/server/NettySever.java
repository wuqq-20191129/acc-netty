package com.wuqq.server;

//import com.wuqq.handler.NettyChannelInitializer;
import com.wuqq.handler.NettyChannelInitializer;
import com.wuqq.handler.NettyIpFilter;
//import com.wuqq.handler.NettyMessageFilter;
import com.wuqq.handler.NettyServerHandler;
import com.wuqq.encode.NettyAccDecoder;
import com.wuqq.encode.NettyAccEncoder;
import com.wuqq.mq.MqReciver;
import com.wuqq.mq.RocketMqSender;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Classname NettySever
 * @Description 启动nettyServer
 * @Date 2021/1/2 20:35
 * @Created by mh
 */
@Component
public class NettySever {

    private static Logger logger = Logger.getLogger(NettySever.class);

    private static  final int PORT = 5001;

    private static EventLoopGroup boss = new NioEventLoopGroup(1); //nio 主线程
    private static EventLoopGroup work = new NioEventLoopGroup(5);//业务线程
    private static ServerBootstrap bootstrap = new ServerBootstrap();//启动器

    ////@Autowired
    //private RocketMqSender rocketMqSender;
    //
    //private NettyIpFilter nettyIpFilter;

   //@Autowired
   // private NettyIpFilter nettyIpFilter;//new 对象并不会自动装配

    //@Autowired
    //private NettyServerHandler nettyServerhandler;

    //public NettySever(){
    //    rocketMqSender =new RocketMqSender();
    //    nettyIpFilter = new NettyIpFilter();
    //}
    //@Autowired
    //private NettyMessageFilter nettyMessageFilter;

    @Autowired
    private NettyChannelInitializer nettyChannelInitializer;

    //@PostConstruct
    public  void run(){


        try {
            bootstrap.group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.option(ChannelOption.SO_BACKLOG,1024);
            //bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
            //绑定handler
            //bootstrap.childHandler(new DiscardServerHandler());
            //bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            //    @Override
            //    protected void initChannel(SocketChannel socketChannel) throws Exception {
            //        ChannelPipeline pipeline = socketChannel.pipeline();
            //        //入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式
            //        pipeline.addFirst(nettyIpFilter);
            //        pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
            //        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536,2,2,2,0));
            //        //pipeline.addLast(new NettyAccDecoder());
            //        //pipeline.addLast(new NettyAccEncoder());
            //        pipeline.addLast(new NettyAccDecoder());
            //        //pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
            //        pipeline.addLast(new ObjectEncoder());
            //        //pipeline.addLast(nettyMessageFilter);
            //        pipeline.addLast(nettyServerhandler);
            //    }
            //});
            bootstrap.childHandler(nettyChannelInitializer);

            ChannelFuture future = bootstrap.bind(PORT).sync();
            logger.info("服务器启动成功，端口"+PORT);

            //consumer报错
            //startMqListener();
            future.channel().closeFuture().sync();

            //startMqListener();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            //关闭服务器监听 释放资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    private void startMqListener() throws MQClientException {
        MqReciver mqReceiver = new MqReciver();
        mqReceiver.start();
    }
}
