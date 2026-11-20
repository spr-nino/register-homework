package com.dino.registermodule.service.serviceImpl;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.LoginUserConstant;
import com.dino.registermodule.base.NumberConstant;
import com.dino.registermodule.config.RedisComponent;
import com.dino.registermodule.domain.entity.LoginRef;
import com.dino.registermodule.domain.entity.LoginUser;
import com.dino.registermodule.domain.result.LoginUserRegisterResult;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.LoginUserService;
import com.dino.registermodule.service.ShortMessageRegisterService;
import com.dino.registermodule.service.VerificationCodeService;
import com.dino.registermodule.utils.RegexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.dino.registermodule.base.LoginUserConstant.RedisShortMessagePrefix.REGISTER_TYPE_PREFIX;


/**
 * 通过手机注册用户服务实现类。
 * 实现 {@link ShortMessageRegisterService}：校验手机号、验证码及手机号是否已被占用，
 * 通过后创建用户与登录映射关系并持久化，最后清除已使用的注册验证码。
 */
@Service
public class ShortMessageRegisterServiceImpl implements ShortMessageRegisterService {
    /** 日志对象。 */
    private static final Logger logger = LoggerFactory.getLogger(ShortMessageRegisterServiceImpl.class);

    /** 登录用户服务，用于主键生成、存在性校验与用户保存。 */
    @Autowired
    private LoginUserService loginUserService;
    /** 验证码服务，用于校验注册验证码。 */
    @Autowired
    private VerificationCodeService verificationCodeService;
    /** Redis 操作组件，用于清除已使用的注册验证码。 */
    @Autowired
    private RedisComponent redisComponent;
    /**
     * 使用手机号与短信验证码注册用户。
     *
     * @param loginMobile 注册手机号
     * @param verificationCode 短信验证码
     * @return 注册结果，包含新生成的登录 ID 与用户 ID
     */
    @Override
    public LoginUserRegisterResult register(String loginMobile, String verificationCode) {
        logger.debug("手机注册");
        if (StringUtils.isEmpty(loginMobile) || !RegexUtils.checkMobile(loginMobile)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "手机号码格式不正确");
        }
        if (StringUtils.isEmpty(verificationCode)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "验证码不能为空");
        }
        if (this.checkLoginName(loginMobile)) {
            //手机号已被注册
            //throw new LoginUserExistException();
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"用户已存在！");
        }
        if(!verificationCodeService.checkRegisterVerificationCode(loginMobile,verificationCode)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"验证码不正确");
        }
        String loginId = this.loginUserService.getPrimaryKey();
        String userId = this.loginUserService.getPrimaryKey();
        String loginRefId = this.loginUserService.getPrimaryKey();
        LoginRef loginRef = new LoginRef();
        loginRef.setId(loginRefId);
        loginRef.setLoginId(loginId);
        loginRef.setUserId(userId);
        LoginUser loginUser = new LoginUser();
        loginUser.setId(loginId);
        loginUser.setLoginMobile(loginMobile);
        loginUser.setLoginStatus(LoginUserConstant.LoginUserStatusType.NORMAL_STATUS);
        loginUser.setLoginUserType(LoginUserConstant.LoginUserType.SHORT_MESSAGE);
        loginUser.setLoginRef(loginRef);
        loginUserService.save(loginUser);
        LoginUserRegisterResult result = new LoginUserRegisterResult();
        result.setLoginId(loginId);
        result.setUserId(userId);
        redisComponent.remove(REGISTER_TYPE_PREFIX+loginMobile);
        return result;
    }

    /**
     * 判断该手机号对应的用户是否已存在。
     * @param loginMobile 注册手机号
     * @return 用户已存在返回 true，否则返回 false
     */
    @Override
    public boolean checkLoginName(String loginMobile) {
        int count = this.loginUserService.countByLoginMobile(loginMobile);
        return count > NumberConstant.Int.INT_ZERO;
    }

    /**
     * 本服务不依赖通用数据访问对象。
     *
     * @return 固定返回 null
     */
    @Override
    public BaseDao<LoginUser> getBaseDao() {
        return null;
    }
}
