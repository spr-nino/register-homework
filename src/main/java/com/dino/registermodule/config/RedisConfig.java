package com.dino.registermodule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类。
 * 创建并配置 RedisTemplate 及其各类操作对象（Hash/String/List/Set/ZSet），
 * 统一设定键与值的序列化方式，并声明 Redis 操作组件。
 */
@Configuration
public class RedisConfig {
    /**
     * 实例化 RedisTemplate 对象，并初始化其序列化方式。
     *
     * @param redisConnectionFactory Redis 连接工厂
     * @return 配置完成的 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }
    /**
     * 设置数据存入 Redis 的序列化方式：键使用字符串序列化，值使用 JDK 序列化。
     *
     * @param redisTemplate 待配置的 RedisTemplate
     * @param factory Redis 连接工厂
     */
    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
    }
    /**
     * 实例化 HashOperations 对象，用于操作 Redis 的 Hash 类型数据。
     *
     * @param redisTemplate RedisTemplate 实例
     * @return Hash 操作对象
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }
    /**
     * 实例化 ValueOperations 对象，用于操作 Redis 的 String（普通键值）类型数据。
     *
     * @param redisTemplate RedisTemplate 实例
     * @return String（值）操作对象
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 实例化 ListOperations 对象，用于操作 Redis 的 List 类型数据。
     *
     * @param redisTemplate RedisTemplate 实例
     * @return List 操作对象
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }
    /**
     * 实例化 SetOperations 对象，用于操作 Redis 的 Set（无序集合）类型数据。
     *
     * @param redisTemplate RedisTemplate 实例
     * @return Set 操作对象
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }
    /**
     * 实例化 ZSetOperations 对象，用于操作 Redis 的 ZSet（有序集合）类型数据。
     *
     * @param redisTemplate RedisTemplate 实例
     * @return ZSet 操作对象
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /**
     * 创建 Redis 操作组件 Bean。
     *
     * @return RedisComponent 实例
     */
    @Bean
    public RedisComponent redisComponent(){
        return new RedisComponent();
    }


}
