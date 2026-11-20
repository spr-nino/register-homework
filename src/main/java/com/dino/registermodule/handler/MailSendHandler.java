package com.dino.registermodule.handler;

import com.dino.registermodule.base.HttpStatus;
import com.dino.registermodule.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.internet.MimeMessage;

/**
 * 邮件发送处理器：封装对 SMTP 网关的调用，用于下发找回密码验证码。
 *
 * <p>安全约束：
 * 1. 只有当 SMTP 主机与账号均配置时才会真正发送，否则显式抛出业务异常，杜绝“只打印日志假装发送”；
 * 2. 验证码只写入发往目标邮箱的邮件正文，绝不出现在应用日志中；
 * 3. 发送异常向上传播，使调用方能够感知失败，避免用户一直等待永远收不到的验证码。</p>
 */
@Component
public class MailSendHandler {

    /** 日志对象（仅记录发送目标与结果，不记录验证码） */
    private static final Logger logger = LoggerFactory.getLogger(MailSendHandler.class);

    /** 邮件主题 */
    private static final String SUBJECT = "找回密码验证码";

    /** Spring Boot 依据 spring.mail.* 自动配置的 JavaMailSender */
    @Autowired
    private JavaMailSender mailSender;
    /** SMTP 主机，未配置（空）时视为邮件服务不可用 */
    @Value("${spring.mail.host:}")
    private String mailHost;
    /** SMTP 账号，未配置（空）时视为邮件服务不可用 */
    @Value("${spring.mail.username:}")
    private String mailUsername;
    /** 发件人地址，留空时回退到 SMTP 账号 */
    @Value("${spring.mail.from:}")
    private String mailFrom;

    /**
     * 向指定邮箱发送找回密码验证码。
     *
     * @param toMail           收件邮箱
     * @param verificationCode 明文验证码（仅写入邮件正文）
     */
    public void sendVerificationCode(String toMail, String verificationCode) {
        // 配置完整性校验：主机或账号缺失时明确失败
        if (StringUtils.isEmpty(mailHost) || StringUtils.isEmpty(mailUsername)) {
            logger.error("邮件服务未配置（spring.mail.host / spring.mail.username 为空），无法向 [{}] 发送验证码", toMail);
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "邮件服务未配置，请联系管理员");
        }
        String from = StringUtils.isEmpty(mailFrom) ? mailUsername : mailFrom;
        String content = "您好，您正在找回密码。本次验证码为：" + verificationCode
                + "。验证码仅用于本次操作，请勿转发他人。若非本人操作，请忽略此邮件。";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(from);
            helper.setTo(toMail);
            helper.setSubject(SUBJECT);
            helper.setText(content, false);
            mailSender.send(message);
            logger.info("找回密码验证码邮件已发送至 [{}]", toMail);
        } catch (BusinessException e) {
            throw e;
        } catch (MailException e) {
            logger.error("验证码邮件发送失败，收件人 [{}]", toMail, e);
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "验证码邮件发送失败，请稍后重试");
        } catch (Exception e) {
            logger.error("构建验证码邮件失败，收件人 [{}]", toMail, e);
            throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, "验证码邮件发送失败，请稍后重试");
        }
    }
}
