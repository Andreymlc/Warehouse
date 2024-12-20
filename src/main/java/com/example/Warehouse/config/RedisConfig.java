package com.example.Warehouse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory())
            .cacheDefaults(cacheConfig)
            .withCacheConfiguration("products", myDefaultCacheConfig(Duration.ofHours(1)))
            .withCacheConfiguration("categories", myDefaultCacheConfig(Duration.ofHours(1)))
            .withCacheConfiguration("warehouses", myDefaultCacheConfig(Duration.ofHours(1)))
            .withCacheConfiguration("purchases", myDefaultCacheConfig(Duration.ofHours(1)))
            .withCacheConfiguration("orders", myDefaultCacheConfig(Duration.ofHours(1)))
            .withCacheConfiguration("cart", myDefaultCacheConfig(Duration.ofDays(60)))
            .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
        return RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(duration);
    }
}
