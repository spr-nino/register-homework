package com.dino.registermodule.domain.param;

import com.dino.registermodule.base.BaseParam;
import lombok.Data;

/**
 * 手机找回密码入参，通过短信验证码重置登录密码。
 */
@Data
public class ShortMessageForgetPasswordParam extends BaseParam {
    /** 注册手机号 */
    private String loginMobile;
    /** 短信验证码 */
    private String verificationCode;
    /** 重置后的新密码 */
    private String newPassword;
}
