package com.alc.diary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    private final Environment env;

    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        System.out.println(env.getProperty("spring.redis.host"));
        if (env.getProperty("spring.redis.host") == null) {
            return new
        }
        return new StringRedisTemplate(connectionFactory);
    }
}
