package com.dino.registermodule.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Redis 连接池配置属性类。
 * 从配置文件读取 redis.maxIdle / maxTotal / blockWhenExhausted / maxWaitMillis / testOnBorrow 等属性，
 * 作为 {@link JedisConfig} 创建连接池配置时的属性来源。
 */
@Component
public class RedisPoolConfigProperties extends PoolConfigProperties {
    /** 连接池中保持空闲的最大连接数。 */
    @Value("${redis.maxIdle}")
    private int maxIdle;
    /** 连接池允许的最大连接总数。 */
    @Value("${redis.maxTotal}")
    private int maxTotal;
    /** 连接耗尽时是否阻塞等待。 */
    @Value("${redis.blockWhenExhausted}")
    private boolean blockWhenExhausted;
    /** 阻塞等待连接的最大时间（毫秒）。 */
    @Value("${redis.maxWaitMillis}")
    private int maxWaitMillis;
    /** 取用连接时是否进行有效性校验。 */
    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    /**
     * 获取最大空闲连接数。
     *
     * @return 最大空闲连接数
     */
    @Override
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * 获取最大连接总数。
     *
     * @return 最大连接数
     */
    @Override
    public int getMaxTotal() {
        return maxTotal;
    }

    /**
     * 连接耗尽时是否阻塞等待。
     *
     * @return 阻塞等待返回 true，否则返回 false
     */
    @Override
    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    /**
     * 获取最大等待时间（毫秒）。
     *
     * @return 最大等待毫秒数
     */
    @Override
    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    /**
     * 取用连接时是否进行有效性校验。
     *
     * @return 校验返回 true，否则返回 false
     */
    @Override
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }
}
