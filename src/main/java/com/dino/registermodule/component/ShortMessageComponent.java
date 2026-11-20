package com.dino.registermodule.component;

import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.base.LoginUserConstant;
import com.dino.registermodule.config.RedisComponent;
import com.dino.registermodule.dao.LoginShortMessageDao;
import com.dino.registermodule.domain.entity.LoginShortMessage;
import com.dino.registermodule.exception.BusinessException;
import com.dino.registermodule.handler.MessageSendHandler;
import com.dino.registermodule.service.LoginShortMessageService;
import com.dino.registermodule.service.VerificationCodeService;
import com.dino.registermodule.utils.DateTimeUtils;
import com.dino.registermodule.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.dino.registermodule.base.LoginUserConstant.LoginShortMessageStatus.SEND;
import static com.dino.registermodule.base.LoginUserConstant.RedisShortMessagePrefix.*;


/**
 * 短信验证码发送组件：校验手机号与短信类型，生成并缓存验证码，
 * 落库发送记录后调用 {@link MessageSendHandler} 完成短信下发。
 */
@Component
public final class ShortMessageComponent {
    /**
     * 是否数字格式
     */
    private static boolean NUMBER_FLAG = true;
    /**
     * 验证码长度
     */
    private static int VERIFICATION_CODE_LENGTH = 6;
    /**
     * 短信验证码存储的时效
     */
    private static long SHORT_MESSAGE_TIMEOUT = 15 * 60;
    /** 验证码生成服务 */
    @Autowired
    private VerificationCodeService verificationCodeService;
    /** Redis 操作组件，用于缓存短信验证码 */
    @Autowired
    private RedisComponent redisComponent;
    /** 短信发送记录服务，用于持久化验证码发送记录 */
    @Autowired
    private LoginShortMessageService loginShortMessageService;
    /** 短信网关发送处理器 */
    @Autowired
    private MessageSendHandler messageSendHandler;

    /** 短信发送记录 DAO */
    @Autowired
    private LoginShortMessageDao loginShortMessageDao;

    /**
     * 发送短信验证码。
     *
     * <p>流程：校验手机号 → 按短信类型选择 Redis 键前缀 → 若缓存中无有效验证码则生成新码、
     * 写入缓存并落库发送记录 → 调用短信网关下发。</p>
     *
     * @param loginMobile      接收验证码的手机号
     * @param shortMessageType 短信类型（登录/注册/忘记密码/绑定/取消绑定），决定 Redis 键前缀
     * @throws BusinessException 手机号格式不正确或短信类型无法匹配时抛出
     */
    public void sendShortMessage(String loginMobile, String shortMessageType) {
        if(StringUtils.isEmpty(loginMobile) || !RegexUtils.checkMobile(loginMobile)){
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"手机号码格式不正确");
        }
        String redisShortMessagePrefix = null;
        //判断短信发送类型
        if (LoginUserConstant.ShortMessageType.LOGIN_TYPE.equalsIgnoreCase(shortMessageType)) {
            redisShortMessagePrefix = LOGIN_TYPE_PREFIX;
        } else if (LoginUserConstant.ShortMessageType.REGISTER_TYPE.equalsIgnoreCase(shortMessageType)) {
            redisShortMessagePrefix = REGISTER_TYPE_PREFIX;
        } else if (LoginUserConstant.ShortMessageType.FORGET_PASSWORD_TYPE.equalsIgnoreCase(shortMessageType)) {
            redisShortMessagePrefix = FORGET_PASSWORD_TYPE_PREFIX;
        } else if (LoginUserConstant.ShortMessageType.BIND_LOGIN_USER_TYPE.equalsIgnoreCase(shortMessageType)) {
            redisShortMessagePrefix = BIND_LOGIN_USER_TYPE_PREFIX;
        } else if(LoginUserConstant.ShortMessageType.CANCEL_BIND_LOGIN_USER_TYPE.equalsIgnoreCase(shortMessageType)){
            redisShortMessagePrefix = CANCEL_BIND_LOGIN_USER_TYPE_PREFIX;
        }else {
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR,"短信验证码类型不匹配");
        }

        String verificationCode = this.redisComponent.get(redisShortMessagePrefix + loginMobile);
        if (StringUtils.isEmpty(verificationCode)) {
            // 缓存中没有有效验证码时才重新生成，并落库一条发送记录
            verificationCode = verificationCodeService.createRandom(NUMBER_FLAG, VERIFICATION_CODE_LENGTH);
            this.redisComponent.set(redisShortMessagePrefix + loginMobile, verificationCode, SHORT_MESSAGE_TIMEOUT);
            LoginShortMessage loginShortMessage = new LoginShortMessage();
            long currentlyDate = DateTimeUtils.getCurrentTimeMillis();
            long endDateTime = currentlyDate + SHORT_MESSAGE_TIMEOUT;
            loginShortMessage.setLoginMobile(loginMobile);
            loginShortMessage.setShortMessageType(shortMessageType);
            loginShortMessage.setVerificationCode(verificationCode);
            loginShortMessage.setSendDateTime(currentlyDate);
            loginShortMessage.setEndDateTime(endDateTime);
            loginShortMessage.setStatus(SEND);
            this.loginShortMessageService.save(loginShortMessage);
        }

        //调用短信发送接口
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("phone",loginMobile);
        paramMap.put("code",verificationCode);
        try{
            messageSendHandler.msgSend(paramMap);
        }catch(Exception e){
            // 短信网关发送失败仅打印异常堆栈，不中断主流程
            e.printStackTrace();
        }
    }
}
