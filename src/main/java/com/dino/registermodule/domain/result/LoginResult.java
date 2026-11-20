package com.dino.registermodule.domain.result;

import com.dino.registermodule.base.BaseResult;
import lombok.Data;

import java.util.List;


/**
 * 用户登录返回值，携带登录账号身份、业务用户标识、会话令牌及已绑定的第三方账号。
 */
@Data
public class LoginResult extends BaseResult {
    /** 登录账号ID */
    private String loginId;
    /** 登录用户类型：1-邮箱(正常) 2-短信 3-第三方 */
    private String loginUserType;
    /** 业务用户类型标识 */
    private String userType;
    /** 关联的业务用户ID */
    private String userId;
    /** 是否注册标识 */
    private boolean isRegister;
    /** 登录成功签发的会话令牌 */
    private String token;
    /** 该登录账号已绑定的第三方账号列表 */
    private List<ThirdPartAccountResult> thirdPartAccountResults;
}
