package com.dino.registermodule.domain.param;

import com.dino.registermodule.base.BaseParam;
import lombok.Data;

/**
 * 注册入参基类，作为各注册方式（邮箱、手机等）入参的公共父类型。
 * <p>本类本身不定义字段，仅用于统一注册参数类型。
 */
@Data
public class RegisterParam extends BaseParam {
}
