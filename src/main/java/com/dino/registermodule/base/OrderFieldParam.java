package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 排序字段参数，描述参与排序的一个字段名及其排序方向（升序/降序）。
 */
public class OrderFieldParam extends BaseParam{
    /** 参与排序的字段名 */
    @Getter
    @Setter
    private String fieldName;
    /** 排序方式，如升序 asc、降序 desc */
    @Getter
    @Setter
    private String orderType;
}
