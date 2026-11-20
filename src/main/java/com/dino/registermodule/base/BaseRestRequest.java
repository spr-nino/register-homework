package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * REST 请求基类，封装所有请求共有的元数据字段，供具体请求对象继承。
 */
public class BaseRestRequest implements Serializable{
    /** 请求时间戳（毫秒） */
    @Getter
    @Setter
    private Long requestTime;
}
