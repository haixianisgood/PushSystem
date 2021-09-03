package com.example.pushsystem.server.handler;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.pushsystem.server.message.UploadMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 处理客户端发来的消息，连接断开时，把客户端的路由信息从redis删除
 * 1、token验证
 * 2、心跳
 * 3、消息确认
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MessageHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    private RedissonClient redis;

    private boolean isInit = false;
    private String pushId;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        UploadMessage message = (UploadMessage) msg;

        String token = message.getToken();
        if(!checkToken(token)) {
            ctx.channel().close();
        }

        if(!isInit()) {
            pushId = message.getPushId();
        }

        if(message.getConfirmId() != null) {
            confirm(message.getConfirmId());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        redis.getMap("map:"+pushId).delete();
        ctx.fireChannelInactive();
    }

    private boolean isInit() {
        return isInit;
    }

    private boolean checkToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        String pushId = jwt.getSubject();

        RLock lock = redis.getLock("lock:"+pushId);

        if(lock.tryLock()) {
            RMap<String, String> map = redis.getMap("map:"+pushId);
            if(map.isExists()) {
                String pushToken = map.get("token");
                lock.unlock();
                return pushToken.equals(token);
            } else {
                lock.unlock();
                return false;
            }
        } else {
            lock.unlock();
            return false;
        }
    }

    private void confirm(String messageId) {

    }
}
