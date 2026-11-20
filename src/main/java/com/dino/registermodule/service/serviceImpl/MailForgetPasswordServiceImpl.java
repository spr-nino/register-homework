package com.dino.registermodule.service.serviceImpl;

import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.NumberConstant;
import com.dino.registermodule.config.RedisComponent;
import com.dino.registermodule.domain.entity.LoginUser;
import com.dino.registermodule.handler.MailSendHandler;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.exception.NotFindLoginUserException;
import com.dino.registermodule.service.ForgetPasswordService;
import com.dino.registermodule.service.LoginUserService;
import com.dino.registermodule.service.VerificationCodeService;
import com.dino.registermodule.utils.MD5Utils;
import com.dino.registermodule.utils.RegexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

import static com.dino.registermodule.base.LoginUserConstant.RedisLoginUserMailPrefix.FORGET_PASSWORD_TYPE_PREFIX;

/**
 * 邮箱找回密码服务实现。
 *
 * <p>负责发送邮箱验证码，以及凭「邮箱 + 验证码」完成密码重置。
 * 验证码统一由 {@link VerificationCodeService} 生成并存入 Redis，
 * 键为 MAIL_FORGET_PASSWORD_ + 邮箱，值为包含 loginId 与 verificationCode 的哈希。</p>
 */
@Service("MailForgetPasswordServiceImpl")
public class MailForgetPasswordServiceImpl extends ForgetPasswordService {

    /** 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(MailForgetPasswordServiceImpl.class);

    /** 用户信息服务 */
    @Autowired
    private LoginUserService loginUserService;
    /** 验证码服务 */
    @Autowired
    private VerificationCodeService verificationCodeService;
    /** Redis 操作组件 */
    @Autowired
    private RedisComponent redisComponent;
    /** 邮件发送处理器 */
    @Autowired
    private MailSendHandler mailSendHandler;

    /**
     * 发送邮箱找回密码验证码。
     *
     * <p>安全修复：原实现生成验证码后仅用日志打印（TODO 未接邮件网关），用户根本收不到验证码，
     * 且验证码明文进日志构成泄露风险。现在生成并存储后通过 {@link MailSendHandler} 真实发送；
     * 邮件服务未配置或发送失败时抛出异常显式失败，日志中不再出现验证码明文。</p>
     *
     * @param loginName 登录邮箱
     */
    @Override
    public void sendMailVerificationCode(String loginName) {
        logger.debug("发送邮箱验证码");
        // 参数校验：邮箱格式必须合法
        if (StringUtils.isEmpty(loginName) || !RegexUtils.checkMail(loginName)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "邮箱格式不正确");
        }
        // 用户必须存在，否则不发送验证码
        if (!this.checkLoginName(loginName)) {
            throw new NotFindLoginUserException();
        }
        // 生成验证码并存入 Redis（键：MAIL_FORGET_PASSWORD_ + 邮箱）
        String verificationCode = verificationCodeService.createMailForgetPasswordVerificationCode(loginName);
        // 真实发送：未配置 SMTP 或发送失败时抛异常，不再静默打印验证码假装成功
        this.mailSendHandler.sendVerificationCode(loginName, verificationCode);
    }

    /**
     * 凭邮箱验证码重置密码。
     *
     * <p>修复说明：
     * 1. 读取验证码的键由 FORGET_PASSWORD_ 改为 MAIL_FORGET_PASSWORD_，与发送端保持一致；
     * 2. 读取方式统一为哈希（getMap），与发送端的存储结构一致；
     * 3. 重置成功后立即删除验证码，避免同一验证码被重复使用；
     * 4. 补充 loginUser 为 null 的判断，避免空指针异常；
     * 5. 新密码按注册时相同的方式做 MD5 加密后再入库。</p>
     *
     * @param loginMail       登录邮箱
     * @param verificationCode 邮箱验证码
     * @param password        新密码（明文）
     */
    @Override
    public void mailForgetPassword(String loginMail, String verificationCode, String password) {
        // 校验邮箱格式
        if (StringUtils.isEmpty(loginMail) || !RegexUtils.checkMail(loginMail)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "邮箱不正确");
        }
        // 校验新密码格式
        if (StringUtils.isEmpty(password) || !RegexUtils.checkPassword(password)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "密码格式错误");
        }
        // 校验验证码非空
        if (StringUtils.isEmpty(verificationCode)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "请输入验证码");
        }
        // 查询用户，不存在则终止
        LoginUser loginUser = this.loginUserService.getLoginUserByLoginMail(loginMail);
        if (loginUser == null || !this.checkLoginName(loginMail)) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "用户不存在！");
        }
        String redisKey = FORGET_PASSWORD_TYPE_PREFIX + loginMail;
        // 从 Redis 读取验证码哈希（与发送端使用相同的键前缀）
        Map<String, Object> dbVerificationCode = redisComponent.getMap(redisKey);
        Object cacheCode = dbVerificationCode == null ? null : dbVerificationCode.get("verificationCode");
        if (cacheCode == null || StringUtils.isEmpty(cacheCode.toString())) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "验证码已过期");
        }
        // 验证码比对（忽略大小写）
        if (!verificationCode.equalsIgnoreCase(cacheCode.toString())) {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "验证码错误");
        }
        // 修改密码：与注册保持一致，使用 MD5 加密
        this.loginUserService.modifyPassword(loginUser.getId(), MD5Utils.hash(password));
        // 验证码一次性使用，重置成功后立即失效
        redisComponent.remove(redisKey);
    }

    /**
     * 判断邮箱对应的用户是否存在。
     *
     * @param loginName 登录邮箱
     * @return 存在返回 true，否则返回 false
     */
    @Override
    protected boolean checkLoginName(String loginName) {
        return loginUserService.countByLoginMail(loginName) > NumberConstant.Int.INT_ZERO;
    }
}
