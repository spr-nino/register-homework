package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 可分页 REST 请求，在单对象请求基础上额外携带分页参数、排序字段列表与查询条件。
 *
 * @param <T> 查询参数类型，需继承 {@link BaseParam}
 */
public class PageableRestRequest<T extends BaseParam> extends RestRequest<T>{
    /** 分页参数（当前页码、每页大小等） */
    @Setter
    @Getter
    private PageParam pageParam;
    /** 排序字段列表，决定结果的排序规则 */
    @Setter
    @Getter
    private List<OrderFieldParam> orderFieldParamList;
    /** 查询条件参数对象 */
    @Setter
    @Getter
    private T param;

}
