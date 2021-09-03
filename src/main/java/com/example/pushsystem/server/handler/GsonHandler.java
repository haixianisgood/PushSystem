package com.example.pushsystem.server.handler;

import com.example.pushsystem.server.message.UploadMessage;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * json字符串 --> UploadMessage对象
 */
public class GsonHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String json = (String) msg;
        Gson gson = new Gson();
        UploadMessage message = gson.fromJson(json, UploadMessage.class);
        ctx.fireChannelRead(message);
    }
}
