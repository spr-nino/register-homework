package com.dino.registermodule.service;

import com.dino.registermodule.base.BaseServices;
import com.dino.registermodule.base.NumberConstant;
import com.dino.registermodule.domain.entity.LoginUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * 验证码服务接口。
 * 定义登录、注册、绑定/解绑手机与邮箱、找回密码等场景的短信/邮箱验证码创建与校验方法，
 * 并提供生成随机验证码字符串的默认实现。
 */
@Transactional(readOnly = false)
public interface VerificationCodeService extends BaseServices<LoginUser> {

    /** 短信验证码长度，取值为 6。 */
    int SHORT_MESSAGE_LENGTH = NumberConstant.Int.INT_SIX;

    /** 邮箱验证码长度，取值为 20。 */
    int MAIL_LENGTH = NumberConstant.Int.INT_TWENTY;

    /** 随机字符串生成标志：不限制为纯数字。 */
    boolean NUMBER_FLAG_FALSE = Boolean.FALSE;
    /** 随机字符串生成标志：仅生成纯数字。 */
    boolean NUMBER_FLAG_TRUE = Boolean.TRUE;

    /** 纯数字字符集，用于生成纯数字验证码。 */
    String NUMBER = "1234567890";

    /** 数字加小写字母字符集，用于生成混合验证码（已剔除部分易混淆字符）。 */
    String NUMBER_STRING = "1234567890abcdefghijkmnpqrstuvwxyz";

    /**
     * 校验登录短信验证码是否正确。
     *
     * @param loginMobile 登录手机号
     * @param verificationCode 待校验的验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkLoginVerificationCode(String loginMobile, String verificationCode);

    /**
     * 创建登录验证码
     * @param loginMobile 登录手机号码
     * @return 登录验证码
     */
    String createLoginVerificationCode(String loginMobile);

    /**
     * 检查绑定手机验证码是否正确。
     * @param loginMobile 手机号码
     * @param verificationCode 待校验的验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkBindMobileVerificationCode(String loginMobile, String verificationCode);

    /**
     * 检查取消绑定手机验证码是否正确。
     *
     * @param loginMobile 手机号码
     * @param verificationCode 待校验的验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkCancelBindMobileVerificationCode(String loginMobile, String verificationCode);
    /**
     * 创建绑定手机号码验证码
     * @param loginMobile 手机号码
     * @return 绑定手机验证码
     */
    String createBindMobileVerificationCode(String loginMobile);
    /**
     * 创建取消绑定手机号码验证码。
     * @param loginMobile 手机号码
     * @return 取消绑定手机验证码
     */
    String createCancelBindMobileVerificationCode(String loginMobile);

    /**
     * 校验短信找回密码验证码是否正确。
     *
     * @param loginMobile 登录手机号
     * @param verificationCode 待校验的验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkForgetPasswordVerificationCode(String loginMobile, String verificationCode);

    /**
     * 创建找回密码验证码
     * @param loginMobile 手机号码
     * @return 找回密码验证码
     */
    String createForgetPasswordVerificationCode(String loginMobile);

    /**
     * 校验注册短信验证码是否正确。
     *
     * @param loginMobile 注册手机号
     * @param verificationCode 待校验的验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkRegisterVerificationCode(String loginMobile, String verificationCode);

    /**
     * 创建注册验证码
     * @param loginMobile 手机号码
     * @return 注册验证码
     */
    String createRegisterVerificationCode(String loginMobile);


    /**
     * 校验绑定邮箱验证码是否正确。
     *
     * @param loginMobile 手机号码
     * @param loginId 登录用户主键
     * @param verificationCode 待校验的邮箱验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkBindMailVerificationCode(String loginMobile, String loginId, String verificationCode);

    /**
     * 校验取消绑定邮箱验证码是否正确。
     *
     * @param loginMobile 手机号码
     * @param loginId 登录用户主键
     * @param verificationCode 待校验的邮箱验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkCancelBindMailVerificationCode(String loginMobile, String loginId, String verificationCode);


    /**
     * 创建绑定邮箱验证码
     * @param loginMail 登录邮箱
     * @return 邮箱验证码
     */
    String createBindMailVerificationCode(String loginMail);

    /**
     * 创建取消绑定邮箱验证码。
     * @param loginMail 登录邮箱
     * @return 取消绑定邮箱验证码
     */
    String createCancelBindMailVerificationCode(String loginMail);

    /**
     * 校验邮箱找回密码验证码是否正确。
     *
     * @param loginMobile 登录手机号
     * @param loginId 登录用户主键
     * @param verificationCode 待校验的邮箱验证码
     * @return 验证码正确返回 true，否则返回 false
     */
    boolean checkMailForgetPasswordVerificationCode(String loginMobile, String loginId, String verificationCode);


    /**
     * 创建邮箱找回密码验证码
     * @param loginMail 登录邮箱
     * @return 邮箱找回密码验证码
     */
    String createMailForgetPasswordVerificationCode(String loginMail);


    /**
     * 创建指定长度的随机字符串，并保证其中至少包含 2 个数字字符。
     *
     * @param numberFlag 为 true 时仅从数字字符中生成，否则从数字与小写字母混合字符集中生成
     * @param length 生成字符串的长度
     * @return 随机字符串
     */
    default String createRandom(boolean numberFlag, int length) {
        String retStr = null;
        // 根据标志选择纯数字或数字字母混合字符集
        String strTable = numberFlag ? NUMBER : NUMBER_STRING;
        int len = strTable.length();
        // 循环生成，直到结果中数字字符数不少于 2 个
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);
        return retStr;
    }
}
