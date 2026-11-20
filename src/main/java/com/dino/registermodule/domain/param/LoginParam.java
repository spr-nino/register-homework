package com.dino.registermodule.domain.param;

import com.dino.registermodule.base.BaseParam;
import lombok.Data;

/**
 * 登录入参基类，作为账号密码、手机验证码、第三方等各登录方式入参的公共父类型。
 * <p>本类本身不定义字段，仅用于统一登录参数类型。
 */
@Data
public class LoginParam extends BaseParam {
}
