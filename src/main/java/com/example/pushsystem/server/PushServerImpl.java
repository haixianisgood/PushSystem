package com.example.pushsystem.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PushServerImpl implements PushServer{
    private ConcurrentHashMap<String, ChannelPipeline> pipelineMap;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private final ServerBootstrap bootstrap;
    private ChannelFuture future;

    public PushServerImpl(ChannelInitializer<NioSocketChannel> initializer,
                          ConcurrentHashMap<String, ChannelPipeline> pipelineMap) {
        this.pipelineMap = pipelineMap;
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(initializer);
    }

    @Override
    public void push(String pushId, byte[] message) {
        ChannelPipeline pipeline = pipelineMap.get(pushId);
        if(pipeline != null) {
            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(message.length);
            pipeline.writeAndFlush(byteBuf).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    byteBuf.release();
                }
            });
        }
    }

    @Override
    public void start(int port) {
        future = bootstrap.bind(port);
    }

    @Override
    public void stop() {
        future.channel().close();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
