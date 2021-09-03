package com.example.pushsystem.mq;

import com.example.pushsystem.server.PushServer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消费者，用于接收MQ发过来的消息，然后推送到客户端中
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "DefaultPushGroup",
        topic = "PushSystem",
        selectorExpression = "PushServer1")
public class MessageListener implements RocketMQListener<MessageExt> {
    @Autowired
    private PushServer pushServer;

    @Override
    public void onMessage(MessageExt message) {
        String pushId = message.getProperty("pushId");
        if(pushId != null) {
            pushServer.push(pushId, message.getBody());
        }
    }
}
