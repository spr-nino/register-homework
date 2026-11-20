package com.dino.registermodule.domain.result;

import com.dino.registermodule.base.BaseResult;
import lombok.Data;

/**
 * 第三方账号返回值，描述登录账号绑定的第三方平台账号信息。
 */
@Data
public class ThirdPartAccountResult extends BaseResult {
    /** 第三方平台返回的用户唯一标识 */
    private String openId;
    /** 第三方平台类型，如微信、QQ、微博 */
    private String thirdPartyAccountType;
    /** 第三方账号绑定记录的主键ID */
    private String id;
}
