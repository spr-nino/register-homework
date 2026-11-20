package com.dino.registermodule.service.serviceImpl;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.dao.LoginShortMessageDao;
import com.dino.registermodule.domain.entity.LoginShortMessage;
import com.dino.registermodule.service.LoginShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 手机短信记录服务实现类。
 * 实现 {@link LoginShortMessageService}，通过注入的 DAO 提供短信记录的通用数据访问入口。
 */
@Service
public class LoginShortMessageServiceImpl implements LoginShortMessageService {
    /** 短信记录数据访问对象。 */
    @Autowired
    private LoginShortMessageDao loginShortMessageDao;

    /**
     * 获取短信记录的数据访问对象。
     *
     * @return 短信记录 DAO
     */
    @Override
    public BaseDao<LoginShortMessage> getBaseDao() {
        return loginShortMessageDao;
    }
}
