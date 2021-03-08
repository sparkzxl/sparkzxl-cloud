package com.github.sparkzxl.authorization.interfaces.dto.tenant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * description: 租户保存对象
 *
 * @author: zhouxinlei
 * @date: 2020-07-27 19:49:46
 */
@Data
@ApiModel(value = "租户保存对象")
public class TenantSaveDTO {

    @ApiModelProperty(value = "租户名称")
    @NotEmpty(message = "租户名称不能为空")
    private String name;

    @ApiModelProperty(value = "logo地址")
    private String logo;

    @ApiModelProperty(value = "有效期为空表示永久")
    private LocalDateTime expirationTime;

    @ApiModelProperty(value = "状态")
    private Boolean status;

    @ApiModelProperty(value = "是否内置")
    private Boolean readonly;

    @ApiModelProperty(value = "租户简介")
    private String describe;

    @ApiModelProperty(value = "用户密码有效期 单位：天 0表示永久有效")
    private Integer passwordExpire;

    @ApiModelProperty(value = "密码输错次数密码输错锁定账号的次数 单位：次")
    private Integer passwordErrorNum;

    @ApiModelProperty(value = "账号锁定时间密码输错${passwordErrorNum}次后，锁定账号的时间单位")
    private String passwordErrorLockTime;

}
