package com.dino.registermodule.controller;

import com.dino.registermodule.base.RestRequest;
import com.dino.registermodule.base.RestResponse;
import com.dino.registermodule.component.LoginComponent;
import com.dino.registermodule.domain.param.NormalLoginParam;
import com.dino.registermodule.domain.param.ShortMessageLoginParam;
import com.dino.registermodule.domain.param.ThirdPartyLoginParam;
import com.dino.registermodule.domain.result.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录接口控制器，对外暴露邮箱登录、短信验证码登录、第三方登录三个入口。
 *
 * <p>仅负责请求参数拆包与响应包装，具体登录逻辑委托给 {@link LoginComponent}。</p>
 *
 * @date 2018/8/24 14:33
 */
@RestController
@Api(produces = "application/json",tags = "LoginUserRestController",description="登录接口")
public class LoginUserRestController extends BaseRestController{
    /** 邮箱+密码登录地址 */
    private static final String EMAIL_LOGIN_URL = "/normal/login";
    /** 短信验证码登录地址 */
    private static final String SHORT_MESSAGE_LOGIN_URL = "/short/message/login";
    /** 第三方登录地址 */
    private static final String THIRD_PART_LOGIN_URL = "/third/part/login";

    /** 登录组件 */
    @Autowired
    private LoginComponent loginComponent;

    /**
     * 邮箱+密码登录。
     *
     * @param request 请求体，param 中包含 loginName（登录名/邮箱）与 password
     * @return 登录结果响应
     */
    @ApiOperation(value="正常登录" , notes = "邮箱和手机+密码登录")
    @RequestMapping(value = EMAIL_LOGIN_URL, method = RequestMethod.POST)
    public RestResponse<LoginResult> emailLogin(@RequestBody RestRequest<NormalLoginParam> request) {
        NormalLoginParam loginParam = request.getParam();
        LoginResult result = loginComponent.login(loginParam);
        return super.createRestResponse(result);
    }

    /**
     * 手机短信验证码登录。
     *
     * @param request 请求体，param 中包含 loginMobile（手机号）与 verificationCode（验证码）
     * @return 登录结果响应
     */
    @ApiOperation(value="短信验证码登录" , notes = "根据LoginComponent调用短信登录方法")
    @RequestMapping(value = SHORT_MESSAGE_LOGIN_URL , method = RequestMethod.POST)
    public@ResponseBody
    RestResponse<LoginResult> shortMessageLogin(@RequestBody RestRequest<ShortMessageLoginParam> request) {
        ShortMessageLoginParam loginParam = request.getParam();
        LoginResult result = loginComponent.login(loginParam);
        return super.createRestResponse(result);
    }

    /**
     * 第三方登录。
     *
     * @param request 请求体，param 中包含 openId（第三方账号标识）与 thirdPartType（第三方类型）
     * @return 登录结果响应
     */
    @ApiOperation(value="第三方登录" , notes = "根据LoginComponent调用第三方登录方法")
    @RequestMapping(value = THIRD_PART_LOGIN_URL , method = RequestMethod.POST)
    public@ResponseBody RestResponse<LoginResult> thirdPartyLogin(@RequestBody RestRequest<ThirdPartyLoginParam> request) {
        ThirdPartyLoginParam loginParam = request.getParam();
        LoginResult result = loginComponent.login(loginParam);
        return super.createRestResponse(result);
    }

}
