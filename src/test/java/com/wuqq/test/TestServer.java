package com.wuqq.test;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;

import io.netty.channel.ChannelInitializer;

import io.netty.channel.ChannelOption;

import io.netty.channel.EventLoopGroup;

import io.netty.channel.nio.NioEventLoopGroup;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.handler.timeout.ReadTimeoutHandler;

import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * @Classname TestServer
 * @Description TODO
 * @Date 2021/1/19 16:11
 * @Created by mh
 */
public class TestServer {


    public static void main(String[] args) {

        // EventLoop 代替原来的 ChannelFactory

        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)

                    .channel(NioServerSocketChannel.class)

                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override

                        public void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(

                                    new TimeServerHandler(),

                                    new WriteTimeoutHandler(10),

                                    //控制写入超时10秒构造参数10表示如果持续10秒钟都没有数据写了，那么就超时。

                                    new ReadTimeoutHandler(10)

                            );

                        }

                    }).option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = serverBootstrap.bind(9090).sync();

            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {

        } finally {

            workerGroup.shutdownGracefully();

            bossGroup.shutdownGracefully();

        }

    }
}
