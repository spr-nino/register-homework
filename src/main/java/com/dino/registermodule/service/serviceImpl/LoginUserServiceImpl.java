package com.dino.registermodule.service.serviceImpl;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.dao.LoginRefDao;
import com.dino.registermodule.dao.LoginUserDao;
import com.dino.registermodule.dao.ThirdPartAccountDao;
import com.dino.registermodule.domain.entity.LoginRef;
import com.dino.registermodule.domain.entity.LoginUser;
import com.dino.registermodule.domain.entity.ThirdPartAccount;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.LoginUserService;
import com.dino.registermodule.utils.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 登录用户服务实现类。
 * 实现 {@link LoginUserService}，负责用户基本信息、登录映射关系与第三方账号的持久化和查询，
 * 查询用户时会一并装载其登录映射信息。
 */
@Service
public class LoginUserServiceImpl implements LoginUserService {
    /** 日志对象。 */
    private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceImpl.class);

    /** 登录用户数据访问对象。 */
    @Autowired
    private LoginUserDao loginUserDao;
    /** 用户登录映射关系数据访问对象。 */
    @Autowired
    private LoginRefDao loginRefDao;
    /** 第三方账号数据访问对象。 */
    @Autowired
    private ThirdPartAccountDao thirdPartAccountDao;

    /**
     * 通过主键查找用户信息，并装载其登录映射信息。
     * @param primaryKey 用户主键
     * @return 对应的登录用户，不存在时返回 null
     */
    @Override
    public LoginUser findOne(String primaryKey) {
        logger.debug("通过主键查找用户信息");
        LoginUser loginUser = this.getBaseDao().selectByPrimaryKey(primaryKey);
        this.initLoginUserRef(loginUser);
        return loginUser;
    }

    /**
     * 通过邮箱获取用户信息，并装载其登录映射信息。
     * @param loginMail 登录邮箱
     * @return 对应的登录用户，不存在时返回 null
     */
    @Override
    public LoginUser getLoginUserByLoginMail(String loginMail) {
        Map<String, String> loginUserParam = new HashMap<>();
        loginUserParam.put("loginMail", loginMail);
        LoginUser loginUser = loginUserDao.selectLoginUser(loginUserParam);
        this.initLoginUserRef(loginUser);

        return loginUser;
    }

    /**
     * 通过手机号获取用户信息，并装载其登录映射信息。
     * @param loginMobile 登录手机号
     * @return 对应的登录用户，不存在时返回 null
     */
    @Override
    public LoginUser getLoginUserByLoginMobile(String loginMobile) {
        Map<String, String> loginUserParam = new HashMap<>();
        loginUserParam.put("loginMobile", loginMobile);
        LoginUser loginUser = loginUserDao.selectLoginUser(loginUserParam);
        this.initLoginUserRef(loginUser);
        return loginUser;
    }

    /**
     * 通过第三方账号信息获取用户信息，并装载其登录映射信息。
     * @param openId 第三方账号唯一标识
     * @param thirdPartAccountType 第三方账号类型
     * @return 对应的登录用户，不存在时返回 null
     */
    @Override
    public LoginUser getLoginUserByOpenId(String openId, String thirdPartAccountType) {
        Map<String, String> loginUserParam = new HashMap<>();
        loginUserParam.put("openId", openId);
        loginUserParam.put("thirdPartAccountType", thirdPartAccountType);
        LoginUser loginUser = loginUserDao.selectLoginUser(loginUserParam);
        this.initLoginUserRef(loginUser);
        return loginUser;
    }

    /**
     * 统计使用该邮箱登录的用户数量。
     * @param loginMail 登录邮箱
     * @return 用户数量
     */
    @Override
    public int countByLoginMail(String loginMail) {
        Map<String, String> loginUserParam = new HashMap<>();
        loginUserParam.put("loginMail", loginMail);
        return this.loginUserDao.countByLoginName(loginUserParam);
    }

    /**
     * 统计使用该手机号登录的用户数量。
     * @param loginMobile 登录手机号
     * @return 用户数量
     */
    @Override
    public int countByLoginMobile(String loginMobile) {
        Map<String, String> loginUserParam = new HashMap<>();
        loginUserParam.put("loginMobile", loginMobile);
        return this.loginUserDao.countByLoginName(loginUserParam);
    }

    /**
     * 修改指定用户的登录密码。
     * @param loginId 登录用户主键
     * @param password 新密码
     */
    @Override
    public void modifyPassword(String loginId, String password) {
        this.loginUserDao.updatePassword(loginId, password);
    }

    /**
     * 已登录场景修改密码：校验旧密码后写入新密码。
     *
     * <p>安全要点：
     * 1. loginId 由调用方从已认证身份传入，本方法不信任请求体中的账号标识；
     * 2. 必须提供旧密码且与库中 MD5 值一致，防止令牌泄露后被直接改密；
     * 3. 新密码在写入前统一做 MD5 加密，与注册、找回密码保持一致。</p>
     *
     * @param loginId     已认证用户的登录主键
     * @param oldPassword 当前明文密码
     * @param newPassword 新明文密码
     */
    @Override
    public void changePasswordWithOldPassword(String loginId, String oldPassword, String newPassword) {
        if (org.springframework.util.StringUtils.isEmpty(loginId)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "登录状态无效，请重新登录");
        }
        // 使用 XML 中实际存在的 selectOne，而非无对应 statement 的 selectByPrimaryKey
        LoginUser loginUser = this.loginUserDao.selectOne(loginId);
        if (loginUser == null) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "登录状态无效，请重新登录");
        }
        if (org.springframework.util.StringUtils.isEmpty(oldPassword)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "请输入旧密码");
        }
        String storedPassword = loginUser.getPassword();
        if (org.springframework.util.StringUtils.isEmpty(storedPassword)
                || !storedPassword.equals(MD5Utils.hash(oldPassword))) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "旧密码不正确");
        }
        this.loginUserDao.updatePassword(loginId, MD5Utils.hash(newPassword));
    }

    /**
     * 保存用户信息，同时保存其登录映射关系；若存在第三方账号则一并批量写入。
     * @param entity 待保存的登录用户
     */
    @Transactional
    @Override
    public void save(LoginUser entity) {
        this.loginUserDao.insert(entity);
        this.loginRefDao.insert(entity.getLoginRef());
        List<ThirdPartAccount> thirdPartAccountList = entity.getThirdPartAccountList();
        // 仅当第三方账号列表非空时才执行批量插入
        if (!CollectionUtils.isEmpty(thirdPartAccountList)) {
            this.thirdPartAccountDao.batchInsert(thirdPartAccountList);
        }
    }

    /**
     * 装载用户的登录映射信息，用户为空时直接返回。
     * @param loginUser 待装载映射信息的登录用户
     */
    private void initLoginUserRef(LoginUser loginUser) {
        if (loginUser == null) {
            return;
        }
        String loginId = loginUser.getId();
        LoginRef loginRef = this.loginRefDao.selectLoginRefByLoginId(loginId);
        loginUser.setLoginRef(loginRef);
    }

    /**
     * 获取登录用户的数据访问对象。
     *
     * @return 登录用户 DAO
     */
    @Override
    public BaseDao<LoginUser> getBaseDao() {
        return loginUserDao;
    }
}
