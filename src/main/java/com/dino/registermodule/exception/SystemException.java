package com.dino.registermodule.exception;

/**
 * 系统异常，用于承载系统级、技术性错误（与业务异常相区分）。
 * <p>通过 messageCode 标识具体的系统错误类型，message 为错误描述。
 */
public class SystemException extends RuntimeException{
    /** 系统错误码，用于定位具体的系统错误类型 */
    private String messageCode;
    /**
     * 构造一个无参系统异常。
     */
    public SystemException(){

    }
    /**
     * 根据错误码与描述信息构造系统异常。
     *
     * @param messageCode 系统错误码
     * @param message 错误描述信息
     */
    public SystemException(String messageCode, String message) {
        super(message);
        this.messageCode = messageCode;
    }
}
