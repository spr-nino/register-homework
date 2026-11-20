package com.dino.registermodule.domain.entity;

import com.dino.registermodule.base.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
/**
 * 用户登录实体类，保存登录账号的身份凭证、绑定信息、登录状态与最近登录记录。
 */
public class LoginUser extends BaseEntity {
    /** 登录用户名（账号密码登录使用） */
    private String loginName;
    /** 绑定的手机号 */
    private String loginMobile;
    /** 绑定的邮箱 */
    private String loginMail;
    /** 登录密码（加密后存储） */
    private String password;
    /** 登录状态：1-正常 2-冻结 3-密码输入错误次数过多 4-废弃 */
    private String loginStatus;
    /** 最近一次登录时间 */
    private Date lastLoginTime;
    /** 最近一次登录 IP */
    private String lastLoginIp;
    /** 最近一次登录设备 */
    private String lastLoginDevice;
    /** 账号注册时间 */
    private Date registerTime;
    /** 登录用户类型：1-邮箱(正常) 2-短信 3-第三方 */
    private String loginUserType;
    /** 关联的业务用户ID */
    private String userId;
    /** 登录账号与业务用户的映射关系 */
    private LoginRef loginRef;
    /** 该登录账号绑定的第三方账号列表 */
    private List<ThirdPartAccount> thirdPartAccountList;
}
