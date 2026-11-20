package com.dino.registermodule.controller;


import com.dino.registermodule.base.BaseRestResponse;
import com.dino.registermodule.base.RestResponse;

/**
 * REST 控制器基类，为子类提供统一的响应对象构造方法。
 */
public abstract class BaseRestController {

    /**
     * 构造一个携带业务结果数据的响应。
     *
     * @param result 业务结果数据
     * @param <T>    结果数据类型
     * @return 包装了 result 的响应对象
     */
    protected <T> RestResponse<T> createRestResponse(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    /**
     * 构造一个不含业务数据的空响应（仅含状态信息）。
     *
     * @return 空的通用响应对象
     */
    protected BaseRestResponse createRestResponse(){
        return new BaseRestResponse();
    }
}
