package com.dino.registermodule.domain.param;

import com.dino.registermodule.base.BaseParam;
import lombok.Data;

/**
 * 发送短信验证码入参，指定接收手机号与验证码用途。
 */
@Data
public class ShortMessageVerificationCodeParam extends BaseParam {
    /** 接收验证码的手机号 */
    private String loginMobile;
    /** 短信用途：1-登录 2-注册 3-找回密码 4-绑定 5-取消绑定 */
    private String shortMessageType;
}
