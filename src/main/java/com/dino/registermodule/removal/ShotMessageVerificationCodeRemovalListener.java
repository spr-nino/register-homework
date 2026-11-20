package com.dino.registermodule.removal;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

/**
 * 短信验证码缓存移除监听器。
 * 实现 Guava Cache 的 {@link RemovalListener}，在短信验证码条目被移除（如过期或被清理）时接收通知；
 * 当前无需额外处理，回调方法为空实现。
 */
public class ShotMessageVerificationCodeRemovalListener implements RemovalListener<String,Object>{
    /**
     * 缓存条目被移除时的回调，当前不做额外处理。
     *
     * @param removalNotification 缓存移除通知，包含被移除的键值及移除原因
     */
    @Override
    public void onRemoval(RemovalNotification<String, Object> removalNotification) {

    }
}
