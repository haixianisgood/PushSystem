package com.example.pushsystem.service;

import com.example.pushsystem.mq.PushProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PushServiceImpl implements PushService{
    @Autowired
    private PushProducer pushProducer;

    @Autowired
    private RouteService routeService;

    @Override
    public void push(String pushId, byte[] message) {
        String serverTag = routeService.pushServerTag(pushId);
        pushProducer.sendMessage(serverTag, message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void pushGroup(String groupId, byte[] message) {
        Map<String, String> groupMap = routeService.groupOnlineMembers(groupId);
        for(Map.Entry<String, String> entry:groupMap.entrySet()) {
            pushProducer.sendMessage(entry.getKey(), message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
