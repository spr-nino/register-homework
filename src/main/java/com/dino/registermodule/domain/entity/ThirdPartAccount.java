package com.dino.registermodule.domain.entity;

import com.dino.registermodule.base.BaseEntity;
import lombok.Data;

/**
 * 第三方登录实体类，保存登录账号与第三方平台账号（openId）的绑定关系。
 */
@Data
public class ThirdPartAccount extends BaseEntity {
    /** 关联的登录账号ID */
    private String loginId;
    /** 第三方平台返回的用户唯一标识 */
    private String openId;
    /** 第三方平台类型，如微信、QQ、微博 */
    private String thirdPartyAccountType;
}
