package com.dino.registermodule.domain.param;

import lombok.Getter;
import lombok.Setter;

/**
 * 正常登陆入参，继承 LoginParam，使用用户名与密码登录。
 */
@Getter
@Setter
public class NormalLoginParam extends LoginParam{
    /** 登录用户名 */
    private String loginName;
    /** 登录密码 */
    private String password;
}
