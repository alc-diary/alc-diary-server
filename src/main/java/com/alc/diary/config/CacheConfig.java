package com.alc.diary.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class CacheConfig {

    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public CacheManager cacheManager() {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("monthlyReport", monthlyReportCacheConfiguration());

        return RedisCacheManager.builder(redisConnectionFactory)
                                .cacheDefaults(defaultCacheConfiguration())
                                .withInitialCacheConfigurations(cacheConfigurations)
                                .build();
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .entryTtl(Duration.ofMinutes(5))
                                      .disableCachingNullValues()
                                      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    private RedisCacheConfiguration monthlyReportCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .entryTtl(Duration.ofMinutes(1))
                                      .disableCachingNullValues()
                                      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
}
