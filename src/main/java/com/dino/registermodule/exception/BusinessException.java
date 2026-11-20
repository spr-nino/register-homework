package com.dino.registermodule.exception;

/**
 * 业务异常，用于承载业务规则校验失败等可预期的错误。
 * <p>通过 messageCode 标识具体的业务错误类型，message 为面向调用方的错误提示。
 */
public class BusinessException extends RuntimeException {
    /** 业务错误码，用于定位具体的业务错误类型 */
    private String messageCode;
    /**
     * 构造一个无参业务异常。
     */
    public BusinessException(){

    }
    /**
     * 根据错误码与提示信息构造业务异常。
     *
     * @param messageCode 业务错误码
     * @param message 面向调用方的错误提示信息
     */
    public BusinessException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }
}
