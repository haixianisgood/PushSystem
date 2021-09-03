package com.example.pushsystem.server;

import com.example.pushsystem.server.handler.MessageHandler;
import com.example.pushsystem.server.handler.GsonHandler;
import com.example.pushsystem.server.handler.LoginHandler;
import com.example.pushsystem.util.SpringUtil;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import org.springframework.stereotype.Component;

/**
 * 二进制数据in --> 长度域解码器 --> 字符串解码器 --> Gson的json处理器 --> 消息处理器 --> 登录处理器
 * 二进制数据out --> 长度域编码器 --> 客户端
 */
@Component
public class PushServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        MessageHandler messageHandler = (MessageHandler) SpringUtil.getBean(MessageHandler.class);
        GsonHandler gsonHandler = new GsonHandler();
        LoginHandler loginHandler = (LoginHandler) SpringUtil.getBean(LoginHandler.class);
        LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder =
                new LengthFieldBasedFrameDecoder(4*1024*1024, 0, 8);
        StringDecoder stringDecoder = new StringDecoder();
        LengthFieldPrepender lengthFieldPrepender = new LengthFieldPrepender(8, false);

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(lengthFieldBasedFrameDecoder,
                lengthFieldPrepender,
                stringDecoder,
                gsonHandler,
                messageHandler,
                loginHandler);
    }
}
