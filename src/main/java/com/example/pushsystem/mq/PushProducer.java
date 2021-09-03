package com.example.pushsystem.mq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PushProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 把消息推送到rocketmq中
     * @param tag 推送目标的socket所在的服务器名
     * @param message 消息实体，二进制数组
     * @param callback 对发送结果的回调
     */
    public void sendMessage(String tag, byte[] message, SendCallback callback) {
        Message mqMessage = new Message("PushSystem", tag, message);
        rocketMQTemplate.asyncSend("PushSystem", mqMessage, callback);
    }
}
