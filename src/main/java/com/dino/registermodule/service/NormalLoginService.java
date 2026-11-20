package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.domain.entity.LoginUser;
import com.dino.registermodule.domain.result.LoginResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户正常登录服务接口。
 * 继承 {@link BaseServices} 的通用数据访问能力，定义以邮箱或手机号加密码方式进行登录的方法。
 */
@Transactional(readOnly = false)
public interface NormalLoginService extends BaseServices<LoginUser> {
    /**
     * 使用邮箱或手机号与密码进行登录。
     *
     * @param loginMail 登录邮箱或手机号
     * @param password 登录密码
     * @return 登录结果，包含用户主键、token 及第三方账号信息
     */
    //邮箱登录
    LoginResult login(String loginMail, String password);

}
