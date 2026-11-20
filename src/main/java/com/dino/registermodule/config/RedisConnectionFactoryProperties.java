package com.dino.registermodule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Redis 连接工厂配置属性类。
 * 从配置文件读取 redis.hostName / port / usePool / password 等属性，
 * 作为 {@link JedisConfig} 创建连接工厂时的属性来源。
 */
@Component
public class RedisConnectionFactoryProperties extends ConnectionFactoryProperties {
    /** Redis 服务主机地址。 */
    @Value("${redis.hostName}")
    private String hostName;
    /** Redis 服务端口。 */
    @Value("${redis.port}")
    private int port;
    /** 是否启用连接池。 */
    @Value("${redis.usePool}")
    private boolean usePool;
    /** Redis 连接密码。 */
    @Value("${redis.password}")
    private String password;

    /**
     * 获取 Redis 服务主机地址。
     *
     * @return 主机地址
     */
    @Override
    public String getHostName() {
        return hostName;
    }

    /**
     * 获取 Redis 服务端口。
     *
     * @return 端口号
     */
    @Override
    public int getPort() {
        return port;
    }

    /**
     * 是否启用连接池。
     *
     * @return 启用连接池返回 true，否则返回 false
     */
    @Override
    public boolean isUsePool() {
        return usePool;
    }

    /**
     * 获取 Redis 连接密码。
     *
     * @return 连接密码
     */
    @Override
    public String getPassword() {
        return password;
    }
}
