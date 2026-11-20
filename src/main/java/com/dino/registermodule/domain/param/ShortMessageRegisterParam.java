package com.dino.registermodule.domain.param;

import lombok.Data;

/**
 * 手机注册入参，继承 RegisterParam，使用手机号与短信验证码注册。
 */
@Data
public class ShortMessageRegisterParam extends RegisterParam{
    /** 注册手机号 */
    private String loginMobile;
    /** 短信验证码 */
    private String verificationCode;
}
