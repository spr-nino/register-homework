package com.dino.registermodule.domain.param;

import lombok.Data;

/**
 * 注册用户入参，继承通用注册参数，补充邮箱与登录密码。
 */
@Data
public class LoginUserRegisterParam extends RegisterParam{
    /** 注册邮箱 */
    private String loginMail;
    /** 登录密码 */
    private String password;
}
