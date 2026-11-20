package com.dino.registermodule.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 第三方登录入参，继承 LoginParam，使用第三方平台的用户标识完成登录。
 */
@Data
@ApiModel(value = "第三方登录入参")
public class ThirdPartyLoginParam extends LoginParam{
    /** 第三方平台返回的用户唯一标识 */
    @ApiModelProperty(value = "openId",required = true)
    private String openId;
    @ApiModelProperty(value = "第三方类型(1:微信，2：QQ 3：微博)",required = true)
    private String thirdPartType;//第三方类型(1:微信，2：QQ 3：微博)
}
