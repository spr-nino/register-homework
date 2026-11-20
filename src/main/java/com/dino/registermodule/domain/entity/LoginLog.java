package com.dino.registermodule.domain.entity;

import com.dino.registermodule.base.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 登录日志实体类，记录一次登录尝试的账号、登录方式、来源平台、IP、时间与结果。
 */
@Data
public class LoginLog extends BaseEntity {
    /** 登录账号ID，关联登录用户 */
    private String loginId;
    /** 登录方式，如账号密码、短信验证码、第三方登录 */
    private Integer loginType;
    /** 登录来源平台，如 Web、App */
    private String loginPlatform;
    /** 登录来源 IP 地址 */
    private String loginIp;
    /** 登录发生的时间 */
    private Date loginTime;
    private Integer loginFlag;//1-成功 2-用户错误 3-密码错误 4-账号锁定
//    private String loginName;
}
