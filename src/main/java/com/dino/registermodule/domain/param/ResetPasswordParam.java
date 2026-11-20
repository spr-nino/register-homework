package com.dino.registermodule.domain.param;

import com.dino.registermodule.base.BaseParam;
import lombok.Data;

/**
 * 修改密码入参，供已登录用户凭旧密码设置新密码。
 *
 * <p>安全说明：登录账号 identity 完全取自服务端已认证身份（token 反查），
 * 因此 {@code loginId} 字段仅作兼容保留，服务端不再信任客户端传入的值。</p>
 */
@Data
public class ResetPasswordParam extends BaseParam {
    /**
     * 待修改密码的登录账号ID。
     *
     * @deprecated 服务端已从登录令牌中解析 loginId，忽略此字段；保留仅为兼容旧请求结构。
     */
    @Deprecated
    private String loginId;
    /** 当前明文密码，用于身份二次确认 */
    private String oldPassword;
    /** 修改后的新明文密码 */
    private String newPassword;
}
