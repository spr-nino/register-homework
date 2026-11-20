package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.domain.entity.ThirdPartAccount;
import com.dino.registermodule.domain.result.LoginResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方登录服务接口。
 * 继承 {@link BaseServices} 对第三方账号实体 {@link ThirdPartAccount} 的通用数据访问能力，
 * 定义以第三方账号 openId 与类型进行登录的方法。
 */
@Transactional(readOnly = false)
public interface ThirdPartyLoginService extends BaseServices<ThirdPartAccount> {
    /**
     * 使用第三方账号进行登录。
     *
     * @param openId 第三方账号唯一标识
     * @param thirdPartAccountType 第三方账号类型
     * @return 登录结果，包含用户主键、token 及注册标识
     */
    //第三方登录
    LoginResult login(String openId, String thirdPartAccountType);
}
