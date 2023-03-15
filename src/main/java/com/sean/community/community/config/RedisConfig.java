package com.sean.community.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) { //会自动注入 如果是在参数中
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // redis 是KV结构 要设置序列化方式
        // key 序列化方式
        template.setKeySerializer(RedisSerializer.string());
        // value 序列化方式
        template.setValueSerializer(RedisSerializer.json());
        // hash key 序列化
        template.setHashKeySerializer(RedisSerializer.string());
        // hash value 序列化
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet(); // 触发配置
        return template;
    }
}
