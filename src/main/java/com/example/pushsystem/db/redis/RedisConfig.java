package com.example.pushsystem.db.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer()
                .setKeepAlive(true)
                .setAddress("redis://127.0.0.1:6379")
                .setTimeout(3*1000);

        return Redisson.create(config);
    }
}
