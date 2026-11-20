package com.dino.registermodule.config;

import com.dino.registermodule.removal.MailVerificationCodeRemovalListener;
import com.dino.registermodule.removal.ShotMessageVerificationCodeRemovalListener;
import com.dino.registermodule.utils.CacheBuild;
import com.dino.registermodule.utils.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码缓存配置类。
 * 负责声明短信验证码、邮箱验证码两类本地缓存的有效期与缓存管理器，
 * 并组装统一的 {@link CacheBuild} 供验证码服务使用。
 */
@Configuration
public class CacheManagerConfig {

    /** 短信验证码有效期（秒），即 15 分钟。 */
    public static int SHOT_MESSAGE_VERIFICATION_CODE_EFFECTIVE_TIME = 15 * 60;

    /** 邮箱验证码有效期（秒），即 24 小时。 */
    public static int MAIL_VERIFICATION_CODE_EFFECTIVE_TIME = 24 * 60 * 60;

    /** Redis 操作组件，注入到缓存构建器中使用。 */
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 创建短信验证码缓存的移除监听器。
     *
     * @return 短信验证码移除监听器实例
     */
    @Bean
    public ShotMessageVerificationCodeRemovalListener shotMessageVerificationCodeRemovalListener() {
        return new ShotMessageVerificationCodeRemovalListener();
    }

    /**
     * 创建邮箱验证码缓存的移除监听器。
     *
     * @return 邮箱验证码移除监听器实例
     */
    @Bean
    public MailVerificationCodeRemovalListener mailVerificationCodeRemovalListener() {
        return new MailVerificationCodeRemovalListener();
    }

    /**
     * 创建短信验证码缓存管理器，并设定短信验证码有效期。
     *
     * @param shotMessageVerificationCodeRemovalListener 短信验证码移除监听器
     * @return 短信验证码缓存管理器
     */
    @Bean("shotMessageVerificationCodeCache")
    public CacheManager shotMessageVerificationCodeCache(ShotMessageVerificationCodeRemovalListener shotMessageVerificationCodeRemovalListener) {
        return new CacheManager(shotMessageVerificationCodeRemovalListener, SHOT_MESSAGE_VERIFICATION_CODE_EFFECTIVE_TIME);
    }

    /**
     * 创建邮箱验证码缓存管理器，并设定邮箱验证码有效期。
     *
     * @param mailVerificationCodeRemovalListener 邮箱验证码移除监听器
     * @return 邮箱验证码缓存管理器
     */
    @Bean("mailVerificationCodeCache")
    public CacheManager mailVerificationCodeCache(MailVerificationCodeRemovalListener mailVerificationCodeRemovalListener) {
        return new CacheManager(mailVerificationCodeRemovalListener, MAIL_VERIFICATION_CODE_EFFECTIVE_TIME);
    }

    /**
     * 创建缓存构建器，按缓存类型登记邮件与短信验证码缓存管理器，并注入 Redis 组件。
     *
     * @param shotMessageVerificationCodeCache 短信验证码缓存管理器
     * @param mailVerificationCodeCache 邮箱验证码缓存管理器
     * @return 配置完成的缓存构建器
     */
    @Bean
    public CacheBuild cacheBuild(@Qualifier("shotMessageVerificationCodeCache") CacheManager shotMessageVerificationCodeCache, @Qualifier("mailVerificationCodeCache") CacheManager mailVerificationCodeCache) {
        CacheBuild cacheBuild = new CacheBuild();
        // 按缓存类型建立映射，便于按类型获取对应的缓存管理器
        Map<CacheBuild.CacheType,CacheManager> cacheManagerMap = new HashMap<>();
        cacheManagerMap.put(CacheBuild.CacheType.MAIL_VERIFICATION_CODE_TYPE,mailVerificationCodeCache);
        cacheManagerMap.put(CacheBuild.CacheType.SHOT_MESSAGE_VERIFICATION_CODE_TYPE,shotMessageVerificationCodeCache);
        cacheBuild.setCacheManagerMap(cacheManagerMap);
        cacheBuild.setRedisComponent(redisComponent);
        return cacheBuild;
    }


}
