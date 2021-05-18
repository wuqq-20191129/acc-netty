package com.wuqq.test;


import io.netty.bootstrap.Bootstrap;

import io.netty.channel.*;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Classname TestClient
 * @Description TODO
 * @Date 2021/1/19 16:14
 * @Created by mh
 */
public class TestClient {




    public static void main(String[] args) throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {

            Bootstrap b = new Bootstrap(); // (1)

            b.group(workerGroup); // (2)

            b.channel(NioSocketChannel.class); // (3)

            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)

            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override

                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ByteArrayDecoder());
                    ch.pipeline().addLast(new ByteArrayEncoder());
                    ch.pipeline().addLast(new TimeClientHandler());

                }

            });


            // Start the client.

            ChannelFuture f = b.connect("172.20.18.34", 5001).sync(); // (5)


            // Wait until the connection is closed.

            f.channel().closeFuture().sync();

        } finally {

            workerGroup.shutdownGracefully();

        }

    }
}

