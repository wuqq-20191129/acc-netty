package com.wuqq.test;


import io.netty.bootstrap.Bootstrap;

import io.netty.channel.*;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioSocketChannel;
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

                    ch.pipeline().addLast(new TimeClientHandler());

                }

            });


            // Start the client.

            ChannelFuture f = b.connect("127.0.0.1", 9090).sync(); // (5)


            // Wait until the connection is closed.

            f.channel().closeFuture().sync();

        } finally {

            workerGroup.shutdownGracefully();

        }

    }
}

