package com.web.pojo.DTO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
public class UserRegisterDTO implements Serializable {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    private String userPassword;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号",required = true)
    private String userTel;

    public UserRegisterDTO() {
    }
}
