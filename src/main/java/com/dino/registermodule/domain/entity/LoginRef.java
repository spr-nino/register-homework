package com.dino.registermodule.domain.entity;

import com.dino.registermodule.base.BaseEntity;
import lombok.Data;

/**
 * 用户影射实体类，将登录账号与业务系统用户建立关联映射。
 * <p>通过 loginId 与 userId 的对应关系关联登录账号和业务用户。
 */
@Data
public class LoginRef extends BaseEntity {
    /** 登录账号ID */
    private String loginId;
    /** 关联的业务用户ID */
    private String userId;
}
