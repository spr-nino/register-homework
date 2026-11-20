package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 单对象参数型 REST 请求，在公共请求信息之上携带一个查询/提交参数对象。
 *
 * @param <T> 参数对象类型，需继承 {@link BaseParam}
 */
public class RestRequest<T extends BaseParam> extends BaseRestRequest{
    /** 请求携带的参数对象 */
    @Getter
    @Setter
    private T param;
}
