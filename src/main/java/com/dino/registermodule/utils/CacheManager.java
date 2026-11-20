package com.dino.registermodule.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Guava Cache 的本地缓存管理器，提供缓存的增删与批量操作。
 */
public class CacheManager {

    /** Guava 本地缓存实例 */
    private Cache<String, Object> cache;

    /**
     * 创建不带过期策略与移除监听器的本地缓存。
     */
    public CacheManager() {
        this.cache = CacheBuilder.newBuilder().build();
    }

    /**
     * 创建带移除监听器的本地缓存。
     *
     * @param removalListener 缓存项被移除时的监听器
     */
    public CacheManager(RemovalListener<String, Object> removalListener) {
        this.cache = CacheBuilder.newBuilder().removalListener(removalListener).build();
    }

    /**
     * 创建带写入过期时间与移除监听器的本地缓存。
     *
     * @param removalListener 缓存项被移除时的监听器
     * @param effectiveTime   写入后经过该时长过期（毫秒）
     */
    public CacheManager(RemovalListener<String, Object> removalListener, long effectiveTime){
        this.cache = CacheBuilder.newBuilder().expireAfterWrite(effectiveTime, TimeUnit.MILLISECONDS).removalListener(removalListener).build();
    }


    /**
     * 保存单个缓存项。
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    public void saveCache(String key, Object value) {
        this.cache.put(key, value);
    }

    /**
     * 批量保存缓存项。
     *
     * @param cacheMapValue 待保存的键值对集合
     */
    public void batchSaveCache(Map<String, Object> cacheMapValue) {
        this.cache.putAll(cacheMapValue);
    }

    /**
     * 删除指定缓存项。
     *
     * @param key 缓存键
     */
    public void remove(String key) {
        this.cache.invalidate(key);
    }

    /**
     * 批量删除指定缓存项。
     *
     * @param keyList 待删除的缓存键集合
     */
    public void removeAll(List<String> keyList) {
        this.cache.invalidateAll(keyList);
    }

    /**
     * 清空本地缓存中的所有缓存项。
     */
    public void removeAll() {
        this.cache.invalidateAll();
    }

    /**
     * 读取缓存值。
     *
     * <p>当前实现固定返回 null，本地缓存的读取实际由 {@link Cache#getValue(String)} 回源 Redis 完成。</p>
     *
     * @param key 缓存键
     * @return 当前实现恒为 null
     */
    public Object getValue(String key) {
       return null;
    }

}
