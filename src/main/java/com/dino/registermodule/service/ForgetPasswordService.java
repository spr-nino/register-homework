package com.dino.registermodule.service;

import com.dino.registermodule.base.NumberConstant;
import com.dino.registermodule.exception.SystemException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 找回密码服务抽象基类。
 * 提供短信找回密码、邮箱找回密码与发送邮箱验证码的默认实现（均直接抛出 {@link SystemException}，
 * 由具体子类覆盖实现），并要求子类实现按登录名判断用户是否存在的逻辑。
 */
@Transactional(readOnly = false)
public abstract class ForgetPasswordService {
    /** 数字标志位，具体含义由子类约定。 */
    public static boolean NUMBER_FLAG = Boolean.FALSE;
    /** 找回密码相关验证码的长度，取值为 10。 */
    public static int VERIFICATION_CODE_LENGTH = NumberConstant.Int.INT_TEN;
    /** 找回密码验证码在 Redis 中使用的键前缀。 */
    public static String FORGET_PASSWORD_PREFIX = "FORGET_PASSWORD_";

    /**
     * 通过短信验证码找回密码，校验通过后重置密码。
     *
     * @param loginMobile 登录手机号
     * @param verificationCode 短信验证码
     * @param password 新密码
     */
    //短信忘记密码
    public void shortMessageForgetPassword(String loginMobile,String verificationCode,String password){
        throw new SystemException();
    }

    /**
     * 通过邮箱验证码找回密码，校验通过后重置密码。
     *
     * @param loginMail 登录邮箱
     * @param verificationCode 邮箱验证码
     * @param password 新密码
     */
    //邮箱忘记密码
    public void mailForgetPassword(String loginMail,String verificationCode,String password){
        throw new SystemException();
    }

    /**
     * 向指定登录名对应的邮箱发送找回密码验证码。
     *
     * @param loginName 登录名（邮箱）
     */
    //邮箱发送验证码
    public void sendMailVerificationCode(String loginName) {
        throw new SystemException();
    }

    /**
     * 按登录名检查用户是否存在。
     *
     * @param loginName 登录名
     * @return 用户存在返回 true，否则返回 false
     */
    //根据登录名检查用户是否存在
    protected abstract boolean checkLoginName(String loginName);
}
