package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.domain.entity.LoginShortMessage;
import org.springframework.transaction.annotation.Transactional;

/**
 * 手机短信记录服务接口。
 * 继承 {@link BaseServices} 对短信实体 {@link LoginShortMessage} 的通用增删改查能力，
 * 用于持久化登录、注册等场景下的短信记录。
 */
@Transactional(readOnly = false)
public interface LoginShortMessageService extends BaseServices<LoginShortMessage> {
}
