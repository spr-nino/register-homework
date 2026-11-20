package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 单对象结果型 REST 响应，在公共响应状态信息之上携带一个业务结果对象。
 *
 * @param <T> 结果对象类型
 */
public class RestResponse<T> extends BaseRestResponse{
    /** 响应携带的业务结果对象 */
    @Getter
    @Setter
    private T result;
}
