package com.dino.registermodule.domain.param;

import lombok.Data;

/**
 * 手机登录入参，继承 LoginParam，使用手机号与短信验证码登录。
 */
@Data
public class ShortMessageLoginParam extends LoginParam{
    /** 登录手机号 */
    private String loginMobile;
    /** 短信验证码 */
    private String verificationCode;
}
