package com.dino.registermodule.base;

/**
 * 登录用户模块的常量集合，集中定义应用名、登录类型、短信类型、邮件类型、
 * 各类状态值以及对应的 Redis 键前缀。
 */
public interface LoginUserConstant {
    /** 应用（服务）名称 */
    String APPLICATION_NAME="LOGIN-USER-SERVICE";


    /**
     * 登录类型
     */
    interface LoginUserType {
        /**NORMAL：正常登录(邮箱)*/
        String NORMAL = "1";
        /**SHORT_MESSAGE：短信登录*/
        String SHORT_MESSAGE = "2";
        /**THIRD_PART：第三方登录*/
        String THIRD_PART = "3";

    }

    /**
     * 短信类型
     */
    interface ShortMessageType{
        /**短信用途：登录*/
        String LOGIN_TYPE = "1";
        /**短信用途：注册*/
        String REGISTER_TYPE = "2";
        /**短信用途：找回密码*/
        String FORGET_PASSWORD_TYPE="3";
        /**短信用途：绑定登录用户*/
        String BIND_LOGIN_USER_TYPE = "4";
        /**短信用途：取消绑定登录用户*/
        String CANCEL_BIND_LOGIN_USER_TYPE = "5";
    }

    /** 各类短信验证码在 Redis 中存储时使用的键前缀 */
    interface RedisShortMessagePrefix{
        /**登录短信的 Redis 键前缀*/
        String LOGIN_TYPE_PREFIX = "LOGIN_";
        /**注册短信的 Redis 键前缀*/
        String REGISTER_TYPE_PREFIX = "REGISTER_";
        /**找回密码短信的 Redis 键前缀*/
        String FORGET_PASSWORD_TYPE_PREFIX = "FORGET_PASSWORD_";
        /**绑定登录用户短信的 Redis 键前缀*/
        String BIND_LOGIN_USER_TYPE_PREFIX = "BIND_LOGIN_USER_";
        /**取消绑定登录用户短信的 Redis 键前缀*/
        String CANCEL_BIND_LOGIN_USER_TYPE_PREFIX = "CANCEL_BIND_LOGIN_USER_" ;
    }

    /** 短信验证码的校验状态 */
    interface LoginShortMessageStatus{
        /**短信已发送*/
        String SEND = "1";
        /**短信验证中*/
        String PROVING = "2";
        /**短信验证码已失效*/
        String INVALIDATE = "3";
    }

    /**
     * 发送邮件类型
     */
    interface LoginUserMailType{
        /**FORGET_PASSWORD_TYPE:找回密码*/
        String FORGET_PASSWORD_TYPE = "1";
        /**BIND_MAIL_TYPE:绑定邮箱*/
        String BIND_MAIL_TYPE = "2";
        /**CANCEL_BIND_MAIL_TYPE:取消绑定邮箱*/
        String CANCEL_BIND_MAIL_TYPE = "3";
    }

    /**
     * Redis Login User Mail Prefix，各类邮件验证码在 Redis 中存储时使用的键前缀
     */
    interface RedisLoginUserMailPrefix{
        /**FORGET_PASSWORD_TYPE_PREFIX:找回密码前缀*/
        String FORGET_PASSWORD_TYPE_PREFIX="MAIL_FORGET_PASSWORD_";
        /**BIND_MAIL_TYPE_PREFIX:绑定邮箱前缀*/
        String BIND_MAIL_TYPE_PREFIX="MAIL_BIND_MAIL_";
        /**CANCEL_BIND_MAIL_TYPE_PREFIX:取消绑定邮箱前缀*/
        String CANCEL_BIND_MAIL_TYPE_PREFIX="MAIL_CANCEL_BIND_MAIL_";

    }
    /**
     * 用户存在状态类型
     */
     interface LoginUserStatusType{
         /**正常状态*/
         String NORMAL_STATUS="1";
         /**冻结*/
         String FREEZE_STATUS="2";
         /**密码次数输入过多*/
         String MORE_TIMES_STSTUS="3";
         /**废弃*/
         String  ABANDON_STATUS="4";
    }

}
