package com.github.sparkzxl.authorization.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.sparkzxl.database.entity.Entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * description: 租户信息
 *
 * @author: zhouxinlei
 * @date: 2021-02-02 16:08:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tenant_info")
@ApiModel(value = "租户信息对象", description = "")
public class TenantInfo extends Entity<Long> {

    private static final long serialVersionUID = -6955056237245642400L;

    @ApiModelProperty(value = "租户编码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "租户名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "状态")
    @TableField("status")
    private Boolean status;

    @ApiModelProperty(value = "是否内置")
    @TableField("readonly")
    private Boolean readonly;

    @ApiModelProperty(value = "有效期为空表示永久")
    @TableField("expiration_time")
    private LocalDateTime expirationTime;

    @ApiModelProperty(value = "logo地址")
    @TableField("logo")
    private String logo;

    @ApiModelProperty(value = "租户简介")
    @TableField("describe_")
    private String describe;

    @ApiModelProperty(value = "用户密码有效期 单位：天 0表示永久有效")
    @TableField("password_expire")
    private Integer passwordExpire;

    @ApiModelProperty(value = "密码输错次数密码输错锁定账号的次数 单位：次")
    @TableField("password_error_num")
    private Integer passwordErrorNum;

    @ApiModelProperty(value = "账号锁定时间密码输错${passwordErrorNum}次后，锁定账号的时间单位")
    @TableField("password_error_lock_time")
    private String passwordErrorLockTime;

    @ApiModelProperty(value = "管理员账户")
    @TableField(exist = false)
    private AuthUser adminUser;

}
