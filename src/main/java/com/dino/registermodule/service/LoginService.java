package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.domain.entity.LoginUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 登录操作 Service。
 * 作为登录相关服务的公共父接口，继承通用数据访问能力 {@link BaseServices}；
 * 具体的邮箱登录、短信登录、第三方登录由其子接口分别定义。
 * @author jiang_nan
 */
@Transactional(readOnly = false)
public interface LoginService extends BaseServices<LoginUser> {
}
