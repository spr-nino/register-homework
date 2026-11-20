package com.dino.registermodule.service;

import com.dino.registermodule.domain.result.LoginUserRegisterResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户正常注册服务接口。
 * 继承 {@link RegisterService}，定义以邮箱和密码方式注册用户的方法。
 */
@Transactional(readOnly = false)
public interface NormalRegisterService extends RegisterService{
    /**
     * 使用邮箱与密码注册用户。
     *
     * @param loginMail 注册邮箱
     * @param password 注册密码
     * @return 注册结果，包含新生成的登录 ID 与用户 ID
     */
    //邮箱注册
    LoginUserRegisterResult register(String loginMail, String password);

}
