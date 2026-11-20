package com.dino.registermodule.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * REST 响应基类，封装所有响应共有的状态码与描述信息。
 * <p>默认构造时状态为成功（{@link RestResponseStatus#SUCCESS}），消息为 "success"。
 */
public class BaseRestResponse implements Serializable{
    /** 响应状态码，取值为 {@link RestResponseStatus} 中定义的常量，默认成功 */
    @Getter
    @Setter
    private String status = RestResponseStatus.SUCCESS;
    /** 响应描述信息，默认 "success" */
    @Getter
    @Setter
    private String message = "success";

    /**
     * 根据指定的状态码与描述信息构造响应。
     *
     * @param status  响应状态码
     * @param message 响应描述信息
     */
    public BaseRestResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * 默认构造，状态码与消息均为成功状态。
     */
    public BaseRestResponse(){

    }
}
