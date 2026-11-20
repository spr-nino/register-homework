package com.dino.registermodule.utils;


import com.dino.registermodule.config.RedisComponent;

import java.util.Map;

/**
 * 缓存构建器：依据静态注入的缓存管理器映射表与 Redis 组件，
 * 按缓存类型创建对应的 {@link Cache} 实例。
 */
public final class CacheBuild {
    /** 缓存类型到本地缓存管理器的映射 */
    private static Map<CacheType, CacheManager> CACHE_MANAGER_MAP;
    /** Redis 操作组件，作为缓存的二级存储 */
    private static RedisComponent REDIS_COMPONENT;

    /** 默认构造方法，实例创建后由 Spring 通过 setter 注入静态依赖 */
    public CacheBuild() {

    }


    /**
     * 缓存类型枚举，用于区分不同业务场景所使用的缓存实例。
     */
    public enum CacheType {
        /** 邮箱验证码缓存 */
        MAIL_VERIFICATION_CODE_TYPE,
        /** 短信验证码缓存（枚举名沿用原有拼写） */
        SHOT_MESSAGE_VERIFICATION_CODE_TYPE;
    }


    /**
     * 按缓存类型构建缓存实例。
     *
     * @param cacheType 缓存类型，用于选取对应的本地缓存管理器
     * @return 绑定了本地缓存管理器与 Redis 组件的缓存实例
     */
    public static Cache build(CacheType cacheType) {
        CacheManager cacheManager = CACHE_MANAGER_MAP.get(cacheType);
        Cache cache = new Cache(cacheManager, REDIS_COMPONENT);
        return cache;
    }

    /**
     * 注入缓存类型到本地缓存管理器的映射（由 Spring 调用）。
     *
     * @param cacheManagerMap 缓存类型与缓存管理器的映射
     */
    public void setCacheManagerMap(Map<CacheType, CacheManager> cacheManagerMap) {
        CACHE_MANAGER_MAP = cacheManagerMap;
    }

    /**
     * 注入 Redis 操作组件（由 Spring 调用）。
     *
     * @param redisComponent Redis 操作组件
     */
    public void setRedisComponent(RedisComponent redisComponent) {
        REDIS_COMPONENT = redisComponent;
    }
}
