package com.dino.registermodule.service.serviceImpl;

import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.NumberConstant;
import com.dino.registermodule.config.RedisComponent;
import com.dino.registermodule.domain.entity.LoginUser;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.ForgetPasswordService;
import com.dino.registermodule.service.LoginUserService;
import com.dino.registermodule.utils.MD5Utils;
import com.dino.registermodule.utils.RegexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.dino.registermodule.base.LoginUserConstant.RedisShortMessagePrefix.FORGET_PASSWORD_TYPE_PREFIX;


/**
 * 通过短信找回密码服务实现类。
 * 继承 {@link ForgetPasswordService}，校验手机号与密码格式以及 Redis 中的短信验证码后重置用户密码，
 * 成功后清除对应的验证码。
 */
@Service("ShortMessageForgetPasswordServiceImpl")
public class ShortMessageForgetPasswordServiceImpl extends ForgetPasswordService {
    /** 日志对象。 */
    private static final Logger logger = LoggerFactory.getLogger(ShortMessageForgetPasswordServiceImpl.class);

    /** 登录用户服务，用于查询用户与修改密码。 */
    @Autowired
    private LoginUserService loginUserService;
    /** Redis 操作组件，用于读取与清除短信验证码。 */
    @Autowired
    private RedisComponent redisComponent;

    /**
     * 通过短信验证码找回密码：校验手机号、密码与验证码，通过后重置密码并清除验证码。
     *
     * @param loginMobile 登录手机号
     * @param verificationCode 短信验证码
     * @param password 新密码
     */
    @Override
    public void shortMessageForgetPassword(String loginMobile, String verificationCode, String password) {
        logger.debug("通过短信找回密码");
        if(StringUtils.isEmpty(loginMobile) || !RegexUtils.checkMobile(loginMobile)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"手机号码不正确！");
        }
        if(StringUtils.isEmpty(password) || !RegexUtils.checkPassword(password)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"密码格式错误");
        }
        if(StringUtils.isEmpty(verificationCode)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"请输入验证码");
        }
        LoginUser loginUser = this.loginUserService.getLoginUserByLoginMobile(loginMobile);
        if (loginUser == null) {
            //手机用户不存在
            //throw new NotFindLoginUserException();
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"用户不存在！");
        }
        //从redis获取验证码
        String dbVerificationCode = redisComponent.get(FORGET_PASSWORD_TYPE_PREFIX + loginMobile);
        if(StringUtils.isEmpty(dbVerificationCode)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"验证码已过期");
        }
        if(!dbVerificationCode.equalsIgnoreCase(verificationCode)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"验证码错误");
        }
        this.loginUserService.modifyPassword(loginUser.getId(), MD5Utils.hash(password));
        redisComponent.remove(FORGET_PASSWORD_TYPE_PREFIX + loginMobile);
    }

    /**
     * 判断该手机号对应的用户是否存在。
     * @param loginName 登录手机号
     * @return 用户存在返回 true，否则返回 false
     */
    @Override
    protected boolean checkLoginName(String loginName) {
        return loginUserService.countByLoginMobile(loginName) > NumberConstant.Int.INT_ZERO;
    }
}
