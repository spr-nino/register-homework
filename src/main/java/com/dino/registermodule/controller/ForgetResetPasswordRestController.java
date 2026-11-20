package com.dino.registermodule.controller;

import com.dino.registermodule.base.BaseRestResponse;
import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.RestRequest;
import com.dino.registermodule.domain.param.ForgetPasswordParam;
import com.dino.registermodule.domain.param.ResetPasswordParam;
import com.dino.registermodule.domain.param.ShortMessageForgetPasswordParam;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.service.ForgetPasswordService;
import com.dino.registermodule.service.LoginUserService;
import com.dino.registermodule.utils.RegexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 忘记密码 / 重置密码接口控制器。
 *
 * <p>提供邮箱验证码发送、邮箱找回密码、短信找回密码以及凭登录 ID 重置密码四个入口。</p>
 */
@RestController
@Api(produces = "application/json", tags = "ForgetResetPasswordRestController", description = "忘记密码/重置密码接口")
public class ForgetResetPasswordRestController extends BaseRestController {

    /** 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(ForgetResetPasswordRestController.class);

    /** 发送邮箱找回密码验证码 */
    private static final String SEND_MAIL_VERIFICATION_CODE_URL = "/send/mail/verification/code";
    /** 邮箱找回密码 */
    private static final String NORMAL_FORGET_PASSWORD_URL = "/normal/forget/password";
    /** 短信找回密码 */
    private static final String SHORT_MESSAGE_FORGET_PASSWORD_URL = "/short/message/forget/password";
    /** 重置密码 */
    private static final String RESET_PASSWORD_URL = "/reset/password";

    /** 邮箱找回密码服务 */
    @Qualifier("MailForgetPasswordServiceImpl")
    @Autowired
    private ForgetPasswordService mailForgetPasswordService;
    /** 短信找回密码服务 */
    @Qualifier("ShortMessageForgetPasswordServiceImpl")
    @Autowired
    private ForgetPasswordService shortMessageForgetPasswordService;
    /** 用户信息服务 */
    @Autowired
    private LoginUserService loginUserService;

    /**
     * 发送邮箱找回密码验证码。
     *
     * <p>修复说明：原项目没有提供发送邮箱验证码的接口，导致邮箱找回密码流程无法触发第一步。
     * 现补充该入口，供前端在用户填写邮箱后调用。</p>
     *
     * @param request 请求体，param 中需包含 loginMail
     * @return 通用响应
     */
    @RequestMapping(value = SEND_MAIL_VERIFICATION_CODE_URL, method = RequestMethod.POST)
    @ApiOperation(value = "发送邮箱找回密码验证码", notes = "根据邮箱生成并存储找回密码验证码")
    public BaseRestResponse sendMailVerificationCode(@RequestBody RestRequest<ForgetPasswordParam> request) {
        logger.debug("发送邮箱找回密码验证码");
        String loginMail = request.getParam().getLoginMail();
        this.mailForgetPasswordService.sendMailVerificationCode(loginMail);
        return this.createRestResponse();
    }

    /**
     * 邮箱找回密码（凭邮箱 + 验证码重置）。
     *
     * @param request 请求体，包含 loginMail、verificationCode、newPassword
     * @return 通用响应
     */
    @RequestMapping(value = NORMAL_FORGET_PASSWORD_URL, method = RequestMethod.POST)
    @ApiOperation(value = "正常忘记密码", notes = "根据 ForgetPasswordService 调用邮箱找回密码方法")
    public BaseRestResponse forgetPassword(@RequestBody RestRequest<ForgetPasswordParam> request) {
        logger.debug("正常忘记密码");
        ForgetPasswordParam param = request.getParam();
        String loginMail = param.getLoginMail();
        this.mailForgetPasswordService.mailForgetPassword(loginMail, param.getVerificationCode(), param.getNewPassword());
        return this.createRestResponse();
    }

    /**
     * 短信找回密码（凭手机号 + 验证码重置）。
     *
     * @param request 请求体，包含 loginMobile、verificationCode、newPassword
     * @return 通用响应
     */
    @RequestMapping(value = SHORT_MESSAGE_FORGET_PASSWORD_URL, method = RequestMethod.POST)
    @ApiOperation(value = "短信忘记密码", notes = "根据 ForgetPasswordService 调用短信找回密码方法")
    public BaseRestResponse shortMessageForgetPassword(@RequestBody RestRequest<ShortMessageForgetPasswordParam> request) {
        logger.debug("短信忘记密码");
        ShortMessageForgetPasswordParam param = request.getParam();
        shortMessageForgetPasswordService.shortMessageForgetPassword(param.getLoginMobile(), param.getVerificationCode(), param.getNewPassword());
        return this.createRestResponse();
    }

    /**
     * 修改密码（需登录态 + 旧密码）。
     *
     * <p>安全修复：原实现仅凭请求体中的 loginId + newPassword 即可任意改密，无任何鉴权。
     * 现由 Spring Security 强制登录态，loginId 从已认证身份反查（忽略客户端传入值，杜绝越权），
     * 并要求校验旧密码后方可修改。</p>
     *
     * @param request 请求体，包含 oldPassword、newPassword（loginId 由服务端从登录态解析）
     * @return 通用响应
     */
    @RequestMapping(value = RESET_PASSWORD_URL, method = RequestMethod.POST)
    @ApiOperation(value = "修改密码", notes = "已登录用户凭旧密码修改密码，需携带登录令牌")
    public BaseRestResponse resetPassword(@RequestBody RestRequest<ResetPasswordParam> request) {
        logger.debug("修改密码");
        ResetPasswordParam param = request.getParam();
        // 校验新密码格式，避免非法密码入库
        if (StringUtils.isEmpty(param.getNewPassword()) || !RegexUtils.checkPassword(param.getNewPassword())) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "密码格式错误");
        }
        // loginId 取自已认证身份，绝不信任请求体传入的账号，防止横向越权改他人密码
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication == null ? null : authentication.getName();
        // 校验旧密码 + 写入新密码（MD5 加密统一在服务层完成）
        loginUserService.changePasswordWithOldPassword(loginId, param.getOldPassword(), param.getNewPassword());
        return this.createRestResponse();
    }
}
