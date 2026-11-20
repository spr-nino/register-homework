package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 列表结果型 REST 响应，在公共响应状态信息之上携带一组结果对象。
 *
 * @param <T> 结果列表元素类型
 */
public class ListRestResponse<T> extends BaseRestResponse{
    /** 响应携带的结果列表 */
    @Getter
    @Setter
    private List<T> itemList;
}
