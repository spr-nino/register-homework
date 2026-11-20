package com.dino.registermodule.dao;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.domain.entity.LoginUser;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 登录用户（{@link LoginUser}）的数据访问接口，提供登录用户查询、绑定信息与状态更新等操作。
 */
public interface LoginUserDao extends BaseDao<LoginUser> {
    /**
     * 查询用户信息，按邮箱、手机号或第三方 openId 等条件联表查询登录用户。
     *
     * @param loginUserParam 查询条件，可含 loginMail、loginMobile、openId、thirdPartAccountType
     * @return 匹配的登录用户，未找到时返回 null
     */
    LoginUser selectLoginUser(Map<String, String> loginUserParam);

    /**
     * 按登录账号主键查询用户（对应 SqlMap-LoginUser.xml 中已有的 selectOne）。
     *
     * <p>BaseDao 声明的 selectByPrimaryKey 在本模块没有对应 statement，
     * 需要按主键读取时统一使用本方法，避免 Invalid bound statement 异常。</p>
     *
     * @param loginId 登录账号主键
     * @return 匹配的用户，未找到时返回 null
     */
    LoginUser selectOne(@Param("loginId") String loginId);
    /**
     * 查找用户数量，按邮箱或手机号统计已存在的登录账号数。
     *
     * @param loginUserParam 统计条件，可含 loginMail、loginMobile
     * @return 符合条件的登录账号数量
     */
    int countByLoginName(Map<String, String> loginUserParam);
    /**
     * 手机绑定用户，为指定登录账号设置绑定手机号。
     *
     * @param loginId 登录账号ID
     * @param loginMobile 待绑定的手机号
     */
    void bindLoginUserMobile(@Param("loginId") String loginId, @Param("loginMobile") String loginMobile);
    /**
     * 取消手机绑定用户，清空指定登录账号已绑定的手机号。
     *
     * @param loginId 登录账号ID
     * @param loginMobile 原绑定的手机号
     */
    void cancelBindLoginUserMobile(@Param("loginId") String loginId, @Param("loginMobile") String loginMobile);
    /**
     * 更新密码，按登录账号ID重置登录密码。
     *
     * @param loginId 登录账号ID
     * @param password 新的登录密码
     */
    void updatePassword(@Param("loginId") String loginId, @Param("password") String password);
    /**
     * 邮箱绑定用户，为指定登录账号设置绑定邮箱。
     *
     * @param loginId 登录账号ID
     * @param loginMail 待绑定的邮箱
     */
    void bindLoginUserMail(@Param("loginId") String loginId, @Param("loginMail") String loginMail);
    /**
     * 取消邮箱绑定用户，清空指定登录账号已绑定的邮箱。
     *
     * @param loginId 登录账号ID
     * @param loginMail 原绑定的邮箱
     */
    void cancelBindLoginUserMail(@Param("loginId") String loginId, @Param("loginMail") String loginMail);
    /**
     * 更新用户登录状态：1-正常 2-冻结 3-密码输入错误次数过多 4-废弃。
     *
     * @param loginId 登录账号ID
     * @param loginStatus 新的登录状态
     */
    void updateLoginUserStatus(@Param("loginId") String loginId, @Param("loginStatus") String loginStatus);

}
