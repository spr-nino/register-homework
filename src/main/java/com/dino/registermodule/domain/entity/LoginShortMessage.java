package com.dino.registermodule.domain.entity;

import com.dino.registermodule.base.BaseEntity;
import lombok.Data;

/**
 * 短信验证码记录实体类，保存一次验证码发送所用的手机号、验证码内容、发送与失效时间及状态。
 */
@Data
public class LoginShortMessage extends BaseEntity {
    /** 关联的登录账号ID */
    private String loginId;
    /** 接收验证码的手机号 */
    private String loginMobile;
    /** 验证码内容 */
    private String verificationCode;
    /** 验证码状态：1-已发送 2-验证中 3-已失效 */
    private String status;
    /** 验证码发送时间（时间戳，毫秒） */
    private long sendDateTime;
    /** 验证码失效时间（时间戳，毫秒） */
    private long endDateTime;
    /** 短信用途类型：1-登录 2-注册 3-找回密码 4-绑定 5-取消绑定 */
    private String shortMessageType;
}
