package com.dino.registermodule.config;

/**
 * 连接池配置属性的抽象基类。
 * 定义 Jedis 连接池常用参数的读取契约：最大空闲连接数、最大连接总数、耗尽时阻塞策略、
 * 最大等待时间以及取用连接时是否校验。
 */
public abstract class PoolConfigProperties {

    /**
     * 获取连接池中保持空闲的最大连接数。
     *
     * @return 最大空闲连接数
     */
    public abstract int getMaxIdle();

    /**
     * 获取连接池允许的最大连接总数。
     *
     * @return 最大连接数
     */
    public abstract int getMaxTotal();

    /**
     * 连接耗尽时是否阻塞等待。
     *
     * @return 阻塞等待返回 true，否则返回 false
     */
    public abstract boolean isBlockWhenExhausted();

    /**
     * 获取连接耗尽阻塞时的最大等待时间（毫秒）。
     *
     * @return 最大等待毫秒数
     */
    public abstract int getMaxWaitMillis();

    /**
     * 从连接池取用连接时是否进行有效性校验。
     *
     * @return 校验返回 true，否则返回 false
     */
    public abstract boolean isTestOnBorrow();
}
