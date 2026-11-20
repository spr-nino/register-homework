package com.dino.registermodule.domain.result;

import com.dino.registermodule.base.BaseResult;
import lombok.Data;

/**
 * 用户注册返回值，返回新创建账号的登录ID与关联的业务用户ID。
 */
@Data
public class LoginUserRegisterResult extends BaseResult {
    /** 新注册的登录账号ID */
    private String loginId;
    /** 关联的业务用户ID */
    private String userId;
}
