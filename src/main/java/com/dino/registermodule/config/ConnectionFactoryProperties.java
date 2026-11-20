package com.dino.registermodule.config;

/**
 * Redis 连接工厂配置属性的抽象基类。
 * 定义连接 Redis 所需的主机、端口、连接池开关与密码属性的读取契约，
 * 由具体配置类（如 {@link RedisConnectionFactoryProperties}）从配置文件注入后实现。
 */
public abstract class ConnectionFactoryProperties {

    /**
     * 获取 Redis 服务主机地址。
     *
     * @return 主机地址
     */
    public abstract String getHostName();

    /**
     * 获取 Redis 服务端口。
     *
     * @return 端口号
     */
    public abstract int getPort();

    /**
     * 是否启用连接池。
     *
     * @return 启用连接池返回 true，否则返回 false
     */
    public abstract boolean isUsePool();

    /**
     * 获取 Redis 连接密码。
     *
     * @return 连接密码
     */
    public abstract String getPassword();
}
