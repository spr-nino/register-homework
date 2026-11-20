package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.domain.entity.LoginUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录用户服务接口。
 * 继承 {@link BaseServices} 的通用数据访问能力，并扩展按邮箱、手机号、第三方账号查询用户，
 * 统计指定登录名下的用户数量以及修改密码等操作。
 */
@Transactional(readOnly = false)
public interface LoginUserService extends BaseServices<LoginUser> {
    /**
     * 根据邮箱获取用户信息。
     *
     * @param loginMail 登录邮箱
     * @return 对应的登录用户，不存在时返回 null
     */
    //根据邮箱获取用户信息
    LoginUser getLoginUserByLoginMail(String loginMail);

    /**
     * 根据手机号获取用户信息。
     *
     * @param loginMobile 登录手机号
     * @return 对应的登录用户，不存在时返回 null
     */
    //根据手机获取用户信息
    LoginUser getLoginUserByLoginMobile(String loginMobile);

    /**
     * 根据第三方账号 openId 获取用户信息。
     *
     * @param openId 第三方账号唯一标识
     * @param thirdPartAccountType 第三方账号类型
     * @return 对应的登录用户，不存在时返回 null
     */
    //根据OpenId获取用户信息
    LoginUser getLoginUserByOpenId(String openId, String thirdPartAccountType);

    /**
     * 统计使用该邮箱登录的用户数量。
     *
     * @param loginMail 登录邮箱
     * @return 用户数量
     */
    //计算该邮箱登录的用户个数
    int countByLoginMail(String loginMail);

    /**
     * 统计使用该手机号登录的用户数量。
     *
     * @param loginMobile 登录手机号
     * @return 用户数量
     */
    //计算该手机号码登录的用户个数
    int countByLoginMobile(String loginMobile);

    /**
     * 修改指定用户的登录密码。
     *
     * @param loginId 登录用户主键
     * @param password 新密码
     */
    //修改密码
    void modifyPassword(String loginId, String password);

    /**
     * 在已登录场景下修改密码：先校验旧密码，再写入新密码（内部完成 MD5 加密）。
     *
     * <p>与 {@link #modifyPassword(String, String)} 的区别：本方法接收明文新密码并校验旧密码，
     * 专供需要登录态的“修改密码”入口使用；loginId 必须来自已认证身份，不接受客户端任意指定。</p>
     *
     * @param loginId        已认证用户的登录主键
     * @param oldPassword    当前明文密码，用于身份二次确认
     * @param newPassword    新明文密码
     */
    void changePasswordWithOldPassword(String loginId, String oldPassword, String newPassword);


}
