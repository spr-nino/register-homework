package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页结果数据集合，封装分页元信息（页码、页数、每页大小、总记录数）与当前页的数据列表。
 *
 * @author jiang_nan
 * @version 1.0
 * @param <T> 分页结果元素类型，需继承 {@link BaseResult}
 */
public class PageResult<T extends BaseResult> extends BaseResult{
    /**当前页*/
    @Getter
    @Setter
    private int currentPage;
    /**一共页数*/
    @Getter
    @Setter
    private int totalPage;
    /**一页显示页数*/
    @Getter
    @Setter
    private int pageSize;
    /**总行数*/
    @Getter
    @Setter
    private long totalCount;
    /**数据集*/
    @Getter
    @Setter
    private List<T> items;
}
