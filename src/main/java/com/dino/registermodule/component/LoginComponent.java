package com.dino.registermodule.component;

import com.dino.registermodule.domain.param.LoginParam;
import com.dino.registermodule.domain.param.NormalLoginParam;
import com.dino.registermodule.domain.param.ShortMessageLoginParam;
import com.dino.registermodule.domain.param.ThirdPartyLoginParam;
import com.dino.registermodule.domain.result.LoginResult;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.NormalLoginService;
import com.dino.registermodule.service.ShortMessageLoginService;
import com.dino.registermodule.service.ThirdPartyLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户登录组件：根据传入的登录参数类型，将登录请求分发给对应的登录服务实现。
 *
 * <p>支持三种登录方式：邮箱+密码（普通登录）、手机短信验证码登录、第三方登录；
 * 若参数类型无法识别则抛出 {@link BusinessException}。</p>
 */
@Component
public final class LoginComponent {
    /** 普通（邮箱+密码）登录服务 */
    @Autowired
    private NormalLoginService normalLoginService;
    /** 手机短信验证码登录服务 */
    @Autowired
    private ShortMessageLoginService shortMessageLoginService;
    /** 第三方登录服务 */
    @Autowired
    private ThirdPartyLoginService thirdPartyLoginService;

    /**
     * 根据登录参数类型执行对应方式的登录。
     *
     * @param loginParam 登录参数，其实际类型决定登录方式：
     *                   {@link NormalLoginParam} 邮箱登录、
     *                   {@link ShortMessageLoginParam} 短信登录、
     *                   {@link ThirdPartyLoginParam} 第三方登录
     * @return 登录结果
     * @throws BusinessException 参数类型不属于上述任何一种时抛出
     */
    public LoginResult login(LoginParam loginParam) {
        LoginResult result = null;
        if (loginParam instanceof NormalLoginParam) {
            /*邮箱登录*/
            String loginName = ((NormalLoginParam) loginParam).getLoginName();
            String password = ((NormalLoginParam) loginParam).getPassword();
            result = this.normalLoginService.login(loginName, password);
        } else if (loginParam instanceof ShortMessageLoginParam) {
            /*手机登录*/
            String loginMobile = ((ShortMessageLoginParam) loginParam).getLoginMobile();
            String verificationCode = ((ShortMessageLoginParam) loginParam).getVerificationCode();
            result = this.shortMessageLoginService.login(loginMobile, verificationCode);
        } else if (loginParam instanceof ThirdPartyLoginParam) {
            /*第三方登录*/
            String openId = ((ThirdPartyLoginParam) loginParam).getOpenId();
            String thirdPartType = ((ThirdPartyLoginParam) loginParam).getThirdPartType();
            result = this.thirdPartyLoginService.login(openId,thirdPartType);
        }else{
            throw new BusinessException();
        }
        return result;
    }


}
