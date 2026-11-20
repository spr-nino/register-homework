package com.dino.registermodule.controller;

import com.dino.registermodule.base.RestRequest;
import com.dino.registermodule.base.RestResponse;
import com.dino.registermodule.component.RegisterComponent;
import com.dino.registermodule.domain.param.LoginUserRegisterParam;
import com.dino.registermodule.domain.param.ShortMessageRegisterParam;
import com.dino.registermodule.domain.result.LoginUserRegisterResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册接口控制器，对外提供邮箱注册与手机短信验证码注册两个入口。
 *
 * <p>仅负责请求参数拆包与响应包装，具体注册逻辑委托给 {@link RegisterComponent}。</p>
 *
 * @author shenf
 * @date 2018/8/27 14:14
 */
@RestController
@Api(produces = "application/json",tags = "RegisterRestController",description="注册接口")
public class RegisterRestController extends BaseRestController{
    /** 注册组件 */
    @Autowired
    private RegisterComponent registerComponent;

    /** 邮箱注册地址 */
    private static final String MAIL_REGISTER_URL = "/mail/register/register";
    /** 短信验证码注册地址 */
    private static final String SHORT_MESSAGE_REGISTER_URL = "/short/message/register";

    /**
     * 邮箱注册。
     *
     * @param request 请求体，param 中包含 loginMail（邮箱）与 password
     * @return 注册结果响应
     */
    @RequestMapping(value = MAIL_REGISTER_URL,method = RequestMethod.POST)
    @ApiOperation(value="正常注册(邮箱注册)" , notes = "根据RegisterComponent调用正常注册方法")
    public@ResponseBody
    RestResponse<LoginUserRegisterResult> loginUserRegister(@RequestBody RestRequest<LoginUserRegisterParam> request) {
        LoginUserRegisterParam registerParam = request.getParam();
        LoginUserRegisterResult result = this.registerComponent.register(registerParam);
        return super.createRestResponse(result);
    }

    /**
     * 手机短信验证码注册。
     *
     * @param request 请求体，param 中包含 loginMobile（手机号）与 verificationCode（验证码）
     * @return 注册结果响应
     */
    @RequestMapping(value = SHORT_MESSAGE_REGISTER_URL,method = RequestMethod.POST)
    @ApiOperation(value="短信验证码注册" , notes = "根据RegisterComponent调用短信注册方法")
    public@ResponseBody RestResponse<LoginUserRegisterResult> shortMessageRegister(@RequestBody RestRequest<ShortMessageRegisterParam> request) {
        ShortMessageRegisterParam registerParam = request.getParam();
        LoginUserRegisterResult result = this.registerComponent.register(registerParam);
        return super.createRestResponse(result);
    }
}
