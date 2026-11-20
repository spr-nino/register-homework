package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 列表参数型 REST 请求，在公共请求信息之上携带一组可序列化的参数对象。
 *
 * @param <T> 参数列表元素类型，需实现 {@link Serializable}
 */
public class ListRestRequest<T extends Serializable> extends BaseRestRequest{
    /** 请求携带的参数列表 */
    @Getter
    @Setter
    private List<T> paramList;
}
