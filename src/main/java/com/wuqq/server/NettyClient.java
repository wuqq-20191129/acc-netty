package com.wuqq.server;


import com.wuqq.encode.NettyAccDecoder;
import com.wuqq.encode.NettyAccEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;

/**
 * @Classname NettyClient
 * @Description TODO
 * @Date 2021/1/5 9:30
 * @Created by mh
 */
public class NettyClient {

    private static Logger logger = Logger.getLogger(NettyClient.class);

    private  static  String ServerIp = "172.20.18.34";

    private  static int Port = 5001;

    public static int SIZE = 256;

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            //bootstrap.handler(new LengthFieldBasedFrameDecoder(65536,2,2,2,0));
            //bootstrap.handler(new NettyClientHandler());


            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                          @Override
                          public void initChannel(SocketChannel ch) throws Exception {
                              //ch.pipeline().addLast(new NettyAccDecoder());
                              //ch.pipeline().addLast(new NettyAccEncoder());
                              ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                              ch.pipeline().addLast(new ObjectEncoder());
                              ch.pipeline().addLast(new NettyClientHandler());
                          }
                      });
            ChannelFuture channelFuture =bootstrap.connect(ServerIp,Port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();
        }

    }
}
