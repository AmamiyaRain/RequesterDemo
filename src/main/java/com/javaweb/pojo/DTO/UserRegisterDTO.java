package com.javaweb.pojo.DTO;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserRegisterDTO implements Serializable {
    /**
     * 用户名
     */

    private String userName;
    /**
     * 密码
     */

    private String userPassword;
    /**
     * 用户手机号
     */

    private Integer userTel;

    public UserRegisterDTO() {
    }
}
