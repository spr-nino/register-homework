package com.dino.registermodule.service.serviceImpl;

import com.dino.registermodule.base.BaseDao;
import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.LoginUserConstant;
import com.dino.registermodule.config.RedisComponent;
import com.dino.registermodule.dao.ThirdPartAccountDao;
import com.dino.registermodule.domain.entity.LoginRef;
import com.dino.registermodule.domain.entity.LoginUser;
import com.dino.registermodule.domain.entity.ThirdPartAccount;
import com.dino.registermodule.domain.result.LoginResult;
import com.dino.registermodule.domain.result.ThirdPartAccountResult;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.LoginUserService;
import com.dino.registermodule.service.ShortMessageLoginService;
import com.dino.registermodule.service.VerificationCodeService;
import com.dino.registermodule.utils.RegexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dino.registermodule.base.LoginUserConstant.RedisShortMessagePrefix.LOGIN_TYPE_PREFIX;


/**
 * 用户通过手机短信进行登录的服务实现类。
 * 实现 {@link ShortMessageLoginService}：校验手机号与短信验证码，用户不存在时自动注册新用户，
 * 登录成功后生成 token 并缓存登录信息，同时清除已使用的登录验证码。
 */
@Service
public class ShortMessageLoginServiceImpl implements ShortMessageLoginService {
    /** 日志对象。 */
    private static final Logger logger = LoggerFactory.getLogger(ShortMessageLoginServiceImpl.class);
    /** 登录 token 在 Redis 中的有效期（秒）。 */
    private static long TOKEN_EFFECTIVE_TIME = 100000000 * 60 * 60;
    /** 登录 token 在 Redis 中的键前缀。 */
    public static String TOKEN = "LG_";
    /** 登录用户服务，用于查询/保存用户与生成主键。 */
    @Autowired
    private LoginUserService loginUserService;
    /** 验证码服务，用于校验登录验证码。 */
    @Autowired
    private VerificationCodeService verificationCodeService;
    /** Redis 操作组件，用于缓存登录信息与清除验证码。 */
    @Autowired
    private RedisComponent redisComponent;
    /** 第三方账号数据访问对象，用于查询用户绑定的第三方账号。 */
    @Autowired
    private ThirdPartAccountDao thirdPartAccountDao;

    /**
     * 使用手机号与短信验证码登录：用户不存在时自动注册，成功后返回登录结果并缓存登录信息。
     *
     * @param loginMobile 登录手机号
     * @param verificationCode 短信验证码
     * @return 登录结果，包含用户主键、token、注册标识及第三方账号信息
     */
    @Override
    public LoginResult login(String loginMobile, String verificationCode) {
        logger.debug("手机短信验证码登录");
        if (StringUtils.isEmpty(loginMobile) || !RegexUtils.checkMobile(loginMobile)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "手机号不正确");
        }
        if (StringUtils.isEmpty(verificationCode) || !verificationCodeService.checkLoginVerificationCode(loginMobile, verificationCode)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "验证码不正确");
        }
        LoginResult loginResult = new LoginResult();
        //通过手机号拿到用户信息
        LoginUser loginUser = this.loginUserService.getLoginUserByLoginMobile(loginMobile);
        LoginRef loginRef;
        String loginId;
        String userId;
        String loginUserType = LoginUserConstant.LoginUserType.SHORT_MESSAGE;
        if (loginUser == null) {
            /*用户不存在新增用户到数据库*/
            loginResult.setRegister(false);
            loginId = this.loginUserService.getPrimaryKey();
            userId = this.loginUserService.getPrimaryKey();
            String loginRefId = this.loginUserService.getPrimaryKey();
            //Login User依赖User
            loginRef = new LoginRef();
            loginRef.setId(loginRefId);
            loginRef.setUserId(userId);
            loginRef.setLoginId(loginId);
            loginUser = new LoginUser();
            loginUser.setLoginMobile(loginMobile);
            loginUser.setLoginUserType(loginUserType);
            loginUser.setId(loginId);
            loginUser.setLoginRef(loginRef);
            this.loginUserService.save(loginUser);
        } else {
            /*用户存在返回longId userId*/
            loginResult.setRegister(true);
            userId = loginUser.getLoginRef().getUserId();
            loginId = loginUser.getId();
        }
        Map<String ,String> map = new HashMap<>();
        map.put("loginId",loginId);
        map.put("userId", userId);
        loginResult.setUserId(userId);
        loginResult.setLoginId(loginId);
        loginResult.setLoginUserType(loginUserType);
        //通过LoginId查询第三方列表
        List<ThirdPartAccount> thirdPartAccountList = this.thirdPartAccountDao.selectThirdPartAccountByLoginId(loginId);
        //填充List<ThirdPartAccountResult>信息
        List<ThirdPartAccountResult> thirdPartAccountResults = new ArrayList<>();
        for (ThirdPartAccount thirdPartAccount : thirdPartAccountList){
            ThirdPartAccountResult thirdPartAccountResult = new ThirdPartAccountResult();
            BeanUtils.copyProperties(thirdPartAccount,thirdPartAccountResult);
            thirdPartAccountResults.add(thirdPartAccountResult);
        }
        //将第三方列表信息塞入loginResult中
        loginResult.setThirdPartAccountResults(thirdPartAccountResults);
        loginResult.setToken(loginUserService.getPrimaryKey());
        /*将loginId userId放入redis*/
        redisComponent.set(TOKEN + loginResult.getToken(), map, TOKEN_EFFECTIVE_TIME );
        redisComponent.remove(LOGIN_TYPE_PREFIX + loginMobile);
        return loginResult;
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
