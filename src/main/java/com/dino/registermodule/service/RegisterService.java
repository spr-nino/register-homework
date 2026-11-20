package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.domain.entity.LoginUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户注册服务接口。
 * 继承 {@link BaseServices} 的通用数据访问能力，定义注册前按登录名检查用户是否已存在的公共方法，
 * 由邮箱注册、短信注册等具体子接口扩展。
 */
@Transactional(readOnly = false)
public interface RegisterService extends BaseServices<LoginUser> {
    /**
     * 根据登录名检查用户是否已存在。
     *
     * @param loginName 登录名（邮箱或手机号）
     * @return 用户已存在返回 true，否则返回 false
     */
    //根据登录名查找用户是否存在
    boolean checkLoginName(String loginName);
}
