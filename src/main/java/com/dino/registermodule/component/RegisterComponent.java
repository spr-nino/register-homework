package com.dino.registermodule.component;

import com.dino.registermodule.domain.param.LoginUserRegisterParam;
import com.dino.registermodule.domain.param.RegisterParam;
import com.dino.registermodule.domain.param.ShortMessageRegisterParam;
import com.dino.registermodule.domain.result.LoginUserRegisterResult;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.NormalRegisterService;
import com.dino.registermodule.service.ShortMessageRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户注册组件：根据传入的注册参数类型，将注册请求分发给对应的注册服务实现。
 *
 * <p>支持邮箱注册与手机短信验证码注册两种方式；
 * 若参数类型无法识别则抛出 {@link BusinessException}。</p>
 */
@Component
public final class RegisterComponent {
    /** 邮箱（普通）注册服务 */
    @Autowired
    private NormalRegisterService normalRegisterService;
    /** 手机短信验证码注册服务 */
    @Autowired
    private ShortMessageRegisterService shortMessageRegisterService;

    /**
     * 根据注册参数类型执行对应方式的注册。
     *
     * @param registerParam 注册参数，其实际类型决定注册方式：
     *                      {@link LoginUserRegisterParam} 邮箱注册、
     *                      {@link ShortMessageRegisterParam} 短信注册
     * @return 注册结果
     * @throws BusinessException 参数类型不属于上述任何一种时抛出
     */
    public LoginUserRegisterResult register(RegisterParam registerParam) {
        LoginUserRegisterResult result = null;
        if (registerParam instanceof LoginUserRegisterParam) {
            //邮箱注册
            String loginMail = ((LoginUserRegisterParam) registerParam).getLoginMail();
            String password = ((LoginUserRegisterParam) registerParam).getPassword();
            result = this.normalRegisterService.register(loginMail,password);
        } else if (registerParam instanceof ShortMessageRegisterParam) {
            //手机短信注册
            String loginMobile = ((ShortMessageRegisterParam) registerParam).getLoginMobile();
            String verificationCode = ((ShortMessageRegisterParam) registerParam).getVerificationCode();
            result = this.shortMessageRegisterService.register(loginMobile,verificationCode);
        }else{
            throw new BusinessException();
        }
        return result;
    }


}
