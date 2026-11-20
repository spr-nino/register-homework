package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 所有数据库实体的公共基类，集中定义各表通用的主键、审计字段与并发控制字段。
 * <p>具体实体继承本类后即可复用 id、创建/更新人、创建/更新时间等公共属性。
 */
public class BaseEntity implements Serializable{
    /** 主键，字符串类型，通常由 UUID 生成 */
    @Getter
    @Setter
    private String id;
    /** 创建人标识 */
    @Getter
    @Setter
    private String createId;
    /** 最后更新人标识 */
    @Getter
    @Setter
    private String updateId;
    /** 记录创建时间 */
    @Getter
    @Setter
    private Date createTime;
    /** 记录最后更新时间 */
    @Getter
    @Setter
    private Date updateTime;
    /** 记录所属的业务归属标识，用于区分不同业务线的数据 */
    @Getter
    @Setter
    private String businessBelong;
    /** 数据版本号，可用于乐观锁并发控制 */
    @Getter
    @Setter
    private String version;
}
