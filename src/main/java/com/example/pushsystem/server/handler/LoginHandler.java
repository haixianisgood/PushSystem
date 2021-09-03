package com.example.pushsystem.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 实现推送登陆功能
 * 把pipeline放到server类的map中
 * 在redis更新自己的推送路由信息
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private RedissonClient redis;
    @Autowired
    private Map<String, ChannelPipeline> pipelineMap;

    @Value("${serverName}")
    private String serverName;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String pushId = (String) msg;
        pipelineMap.put(pushId, ctx.pipeline());
        RLock lock = redis.getLock("lock:"+pushId);

        lock.lock();
        RMap<String, String> map = redis.getMap("map:"+pushId);
        map.put("PushServer:", serverName);
        lock.unlock();

        ctx.pipeline().remove(this);
    }

}
