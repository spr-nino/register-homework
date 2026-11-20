package com.dino.registermodule.domain.param;

import com.dino.registermodule.base.BaseParam;
import lombok.Data;

/**
 * 忘记密码入参，通过邮箱验证码重置登录密码。
 */
@Data
public class ForgetPasswordParam extends BaseParam {
    /** 注册邮箱，用于接收验证码 */
    private String loginMail;
    /** 邮箱收到的验证码 */
    private String verificationCode;
    /** 重置后的新密码 */
    private String newPassword;
}
