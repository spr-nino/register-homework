package com.dino.registermodule.service;

import com.dino.registermodule.domain.result.LoginResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 短信登录服务接口。
 * 继承 {@link LoginService}，定义以手机号加短信验证码方式进行登录的方法。
 */
@Transactional(readOnly = false)
public interface ShortMessageLoginService extends LoginService{
    /**
     * 使用手机号与短信验证码进行登录。
     *
     * @param loginMobile 登录手机号
     * @param verificationCode 短信验证码
     * @return 登录结果，包含用户主键、token 及注册标识
     */
    //手机短信登录
    LoginResult login(String loginMobile, String verificationCode);
}
