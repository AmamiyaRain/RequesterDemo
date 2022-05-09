package com.javaweb.pojo.DAO;

import lombok.Data;

import java.io.Serializable;

@Data
public class FinalUserAccountDAO implements Serializable {
    /**
     * 主键自增id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号
     */
    private Integer userTel;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 注册邮箱
     */
    private String userEmail;
    /**
     * 注册学号
     */
    private String userStuNo;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户权限
     */
    private String userPermission;
    /**
     * 密码盐
     */
    private String userSalt;

    private static final long serialVersionUID = 1L;
}