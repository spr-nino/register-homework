package com.dino.registermodule.service;

import com.dino.registermodule.domain.result.LoginUserRegisterResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 短信注册服务接口。
 * 继承 {@link RegisterService}，定义以手机号加短信验证码方式注册用户的方法。
 */
@Transactional(readOnly = false)
public interface ShortMessageRegisterService extends RegisterService{
    /**
     * 使用手机号与短信验证码注册用户。
     *
     * @param loginMobile 注册手机号
     * @param verificationCode 短信验证码
     * @return 注册结果，包含新生成的登录 ID 与用户 ID
     */
    //手机短信注册
    LoginUserRegisterResult register(String loginMobile, String verificationCode);

}
