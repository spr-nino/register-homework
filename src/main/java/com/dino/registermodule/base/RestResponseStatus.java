package com.dino.registermodule.base;

/**
 * REST 响应状态常量，复用 {@link HttpStatus} 中定义的标准状态码字符串，
 * 为业务响应提供成功、失败与未登录等语义化状态。
 */
public interface RestResponseStatus {
    /**操作成功*/
    String SUCCESS = HttpStatus.OK;
    /**操作失败*/
    String ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    /**未登录*/
    String NOT_LOGIN = HttpStatus.UNAUTHORIZED;
}
