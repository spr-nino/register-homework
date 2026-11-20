package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据库分页参数，描述当前页码、每页记录数以及由页码换算出的数据库查询起始行号。
 *
 * @author jiang_nan
 */
public class PageParam extends BaseParam {
    /** 当前页码，从 1 开始 */
    @Getter
    @Setter
    private int currentPage;
    /** 每页显示的记录数 */
    @Getter
    @Setter
    private int pageSize;
    /** 是否启用分页 */
    @Getter
    @Setter
    private boolean paging;
    /** 数据库查询起始行号，由拷贝构造函数根据页码和每页大小计算得出 */
    @Getter
    private int rowNumber;
    /**
     * 默认构造，创建一个未设置分页信息的空参数对象。
     */
    public PageParam() {

    }

    /**
     * 拷贝构造，复制分页信息并计算数据库查询的起始行号。
     *
     * @param pageParam 源分页参数
     */
    public PageParam(PageParam pageParam) {
        this.currentPage = pageParam.getCurrentPage();
        this.pageSize = pageParam.getPageSize();
        this.paging = pageParam.isPaging();
        // 起始行号 = （当前页码 - 1）* 每页记录数
        this.rowNumber = (currentPage - NumberConstant.Int.INT_ONE) * pageSize;
    }


}
