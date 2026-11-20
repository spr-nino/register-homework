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
import com.dino.registermodule.service.ThirdPartyLoginService;
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

/**
 * 通过第三方账号进行用户登录的服务实现类。
 * 实现 {@link ThirdPartyLoginService}：按 openId 与第三方类型查询用户，不存在时同时创建用户、
 * 登录映射关系与第三方账号记录；登录成功后生成 token 并缓存登录信息。
 */
@Service
public class ThirdPartyLoginServiceImpl implements ThirdPartyLoginService {
    /** 日志对象。 */
    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyLoginServiceImpl.class);
    /** 登录 token 在 Redis 中的有效期（秒）。 */
    private static long TOKEN_EFFECTIVE_TIME = 100000000 * 60 * 60 ;
    /** 登录 token 在 Redis 中的键前缀。 */
    public static String TOKEN_ = "LG_" ;

    /** 登录用户服务，用于查询/保存用户与生成主键。 */
    @Autowired
    private LoginUserService loginUserService;
    /** 第三方账号数据访问对象，用于查询用户绑定的第三方账号。 */
    @Autowired
    private ThirdPartAccountDao thirdPartAccountDao;
    /** Redis 操作组件，用于缓存登录信息。 */
    @Autowired
    private RedisComponent redisComponent;
    /**
     * 使用第三方账号登录：账号不存在时自动创建用户与第三方账号记录，成功后返回登录结果并缓存登录信息。
     *
     * @param openId 第三方账号唯一标识
     * @param thirdPartAccountType 第三方账号类型
     * @return 登录结果，包含用户主键、token、注册标识及第三方账号信息
     */
    @Override
    public LoginResult login(String openId, String thirdPartAccountType) {
        if (StringUtils.isEmpty(openId)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "应用不正确");
        }
        if (StringUtils.isEmpty(thirdPartAccountType)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "第三方登录类型不正确");
        }
        LoginUser loginUser = loginUserService.getLoginUserByOpenId(openId, thirdPartAccountType);
        LoginResult loginResult = new LoginResult();
        LoginRef loginRef;
        String loginId;
        String userId;
        String loginRefId;
        String thirdPartAccountId;
        String loginUserType = LoginUserConstant.LoginUserType.THIRD_PART;
        //判断用户是否为空
        logger.debug("判断用户是否为空");
        if (loginUser == null) {
            /*用户不存在新增用户*/
            loginId = this.loginUserService.getPrimaryKey();
            userId = this.loginUserService.getPrimaryKey();
            loginRefId = this.loginUserService.getPrimaryKey();
            thirdPartAccountId = this.loginUserService.getPrimaryKey();

            loginRef = new LoginRef();
            loginRef.setUserId(userId);
            loginRef.setLoginId(loginId);
            loginRef.setId(loginRefId);


            ThirdPartAccount thirdPartAccount = new ThirdPartAccount();
            thirdPartAccount.setId(thirdPartAccountId);
            thirdPartAccount.setLoginId(loginId);
            thirdPartAccount.setOpenId(openId);
            thirdPartAccount.setThirdPartyAccountType(thirdPartAccountType);
            List<ThirdPartAccount> thirdPartAccountList = new ArrayList<>();
            thirdPartAccountList.add(thirdPartAccount);
            loginUser = new LoginUser();
            loginUser.setLoginUserType(loginUserType);
            loginUser.setId(loginId);
            loginUser.setLoginRef(loginRef);
            loginUser.setThirdPartAccountList(thirdPartAccountList);
            this.loginUserService.save(loginUser);
            loginResult.setRegister(false);
        } else {
            /*用户存在返回loginId、userId*/
//            userId = loginUser.getLoginRef().getUserId();
            userId = loginUser.getUserId();
            loginId = loginUser.getId();
            loginResult.setRegister(true);
        }
        Map<String,String> map = new HashMap<>();
        map.put("loginId",loginId);
        map.put("userId",userId);
        loginResult.setUserId(userId);
        loginResult.setLoginId(loginId);
        loginResult.setLoginUserType(loginUserType);
        //将第三方列表信息塞入ThirdPartAccountResult中
        List<ThirdPartAccount> thirdPartAccountList =this.thirdPartAccountDao.selectThirdPartAccountByLoginId(loginId);
        //填充List<ThirdPartAccountResult>信息
        List<ThirdPartAccountResult> thirdPartAccountResults = new ArrayList<>();
        ////将第三方列表信息塞入loginResult中
        loginResult.setThirdPartAccountResults(thirdPartAccountResults);
        for(ThirdPartAccount thirdPartAccount : thirdPartAccountList){
            ThirdPartAccountResult thirdPartAccountResult = new ThirdPartAccountResult();
            BeanUtils.copyProperties(thirdPartAccount,thirdPartAccountResult);
            thirdPartAccountResults.add(thirdPartAccountResult);

        }
        loginResult.setThirdPartAccountResults(thirdPartAccountResults);
        loginResult.setToken(loginUserService.getPrimaryKey());
        //将loginId和userId存入redis中
        redisComponent.set(TOKEN_ + loginResult.getToken(),map,TOKEN_EFFECTIVE_TIME);
        return loginResult;
    }

    /**
     * 获取第三方账号的数据访问对象。
     *
     * @return 第三方账号 DAO
     */
    @Override
    public BaseDao<ThirdPartAccount> getBaseDao() {
        return thirdPartAccountDao;
    }
}
