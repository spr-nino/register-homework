package com.dino.registermodule.dao;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.domain.entity.LoginRef;
import org.apache.ibatis.annotations.Param;

/**
 * 登录账号与业务用户映射关系（{@link LoginRef}）的数据访问接口，继承通用增删改查能力。
 */
public interface LoginRefDao extends BaseDao<LoginRef> {
    /**
     * 通过登录Id查找用户影射表的信息，返回该登录账号与业务用户的映射记录。
     *
     * @param loginId 登录账号ID
     * @return 匹配的映射记录，未找到时返回 null
     */
    LoginRef selectLoginRefByLoginId(@Param("loginId") String loginId);
}
