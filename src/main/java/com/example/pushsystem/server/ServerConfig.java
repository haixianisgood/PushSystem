package com.example.pushsystem.server;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ServerConfig {
    //推送服务器端口
    @Value("${pushServerPort}")
    private int port;

    /**
     * 推送服务器的bean
     * @param initializer netty的channel初始化
     * @param pipelineMap 用于保存用户socket的map
     * @return 推送服务器实例
     */
    @Bean(destroyMethod = "stop")
    public PushServer pushServer(@Autowired PushServerChannelInitializer initializer,
                                 @Autowired ConcurrentHashMap<String, ChannelPipeline> pipelineMap) {
        PushServer pushServer = new PushServerImpl(initializer, pipelineMap);
        pushServer.start(port);
        return pushServer;
    }

    @Bean(name = "pipelineMap")
    public ConcurrentHashMap<String, ChannelPipeline> pipelineMap() {
        return new ConcurrentHashMap<>(50000);
    }
}
