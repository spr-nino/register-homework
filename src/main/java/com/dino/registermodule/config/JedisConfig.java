package com.dino.registermodule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis 相关配置类。
 * 依据配置属性创建 Jedis 连接池配置与 Redis 连接工厂，供 Spring Data Redis 使用。
 */
@Configuration
public class JedisConfig {

    /**
     * 创建 Jedis 连接池配置，将外部配置的连接池参数写入其中。
     *
     * @param configProperties 连接池配置属性
     * @return 初始化完成的 Jedis 连接池配置
     */
    @Bean
    public JedisPoolConfig poolConfig(PoolConfigProperties configProperties){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(configProperties.getMaxIdle());
        poolConfig.setMaxTotal(configProperties.getMaxTotal());
        poolConfig.setBlockWhenExhausted(configProperties.isBlockWhenExhausted());
        poolConfig.setMaxWaitMillis(configProperties.getMaxWaitMillis());
        poolConfig.setTestOnBorrow(configProperties.isTestOnBorrow());
        return poolConfig;
    }

    /**
     * 创建 Jedis 连接工厂，配置主机、端口、连接池及访问密码。
     *
     * @param poolConfig Jedis 连接池配置
     * @param factoryProperties Redis 连接配置属性
     * @return 初始化完成的 Jedis 连接工厂
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig poolConfig, ConnectionFactoryProperties factoryProperties){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(factoryProperties.getHostName());
        connectionFactory.setPort(factoryProperties.getPort());
        connectionFactory.setUsePool(factoryProperties.isUsePool());
        connectionFactory.setPoolConfig(poolConfig);
        connectionFactory.setPassword(factoryProperties.getPassword());
        return connectionFactory;
    }

}
