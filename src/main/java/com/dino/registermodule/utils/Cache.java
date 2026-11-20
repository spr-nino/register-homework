package com.dino.registermodule.utils;

import com.dino.registermodule.config.RedisComponent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 二级缓存门面：同时操作本地 Guava 缓存与 Redis。
 *
 * <p>写入时两级同时更新；读取时先读本地缓存，为空再回源 Redis；
 * 删除时两级同时清除。</p>
 */
public final class Cache {

    /** 本地缓存管理器 */
    private CacheManager cacheManager;

    /** Redis 操作组件，作为二级缓存 */
    private RedisComponent redisComponent;

    /**
     * 创建缓存实例。
     *
     * @param cacheManager   本地缓存管理器
     * @param redisComponent Redis 操作组件
     */
    public Cache(CacheManager cacheManager, RedisComponent redisComponent) {
        this.cacheManager = cacheManager;
        this.redisComponent = redisComponent;
    }

    /**
     * 保存字符串值到本地缓存与 Redis。
     *
     * @param key           缓存键
     * @param value         缓存值
     * @param effectiveTime Redis 中的有效时长（秒）
     */
    public void save(String key, String value, long effectiveTime) {
        cacheManager.saveCache(key, value);
        this.redisComponent.set(key, value, effectiveTime);
    }

    /**
     * 保存 Map 值到本地缓存与 Redis。
     *
     * @param key           缓存键
     * @param value         Map 类型的缓存值
     * @param effectiveTime Redis 中的有效时长（秒）
     */
    public void save(String key, Map<String,String> value,long effectiveTime){
        cacheManager.saveCache(key,value);
        this.redisComponent.set(key, value, effectiveTime);
    }

    /**
     * 读取字符串缓存：先读本地缓存，为空则回源 Redis。
     *
     * @param key 缓存键
     * @return 缓存的字符串值；两级缓存均未命中时返回 null
     */
    public String getValue(String key){
        String value = (String) this.cacheManager.getValue(key);
        if(StringUtils.isEmpty(value)){
            value = redisComponent.get(key);
        }
        return value;
    }

    /**
     * 读取 Map 缓存：先读本地缓存，为空则回源 Redis。
     *
     * @param key 缓存键
     * @return 缓存的 Map；本地未命中时返回 Redis 中的 Map（Redis 中无数据则为空 Map）
     */
    public Map<String,Object> getMapValue(String key){
        Map<String,Object> value = (Map<String,Object>)this.cacheManager.getValue(key);
        if(CollectionUtils.isEmpty(value)){
            value = redisComponent.getMap(key);
        }
        return value;
    }

    /**
     * 从本地缓存与 Redis 中同时删除指定键。
     *
     * @param key 缓存键
     */
    public void remove(String key){
        this.cacheManager.remove(key);
        this.redisComponent.remove(key);
    }
}
