package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页结果型 REST 响应，在公共响应状态信息之上携带一个分页结果对象。
 *
 * @param <T> 分页结果元素类型，需继承 {@link BaseResult}
 */
public class PageableRestResponse<T extends BaseResult> extends BaseRestResponse{
    /** 响应携带的分页结果 */
    @Getter
    @Setter
    private PageResult<T> result;
}
