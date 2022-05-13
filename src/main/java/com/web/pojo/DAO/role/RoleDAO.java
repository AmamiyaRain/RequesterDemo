package com.web.pojo.DAO.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
    * 用户角色表
    */
@ApiModel(value="用户角色表")
@Data
public class RoleDAO {
    @ApiModelProperty(value="id")
    private Integer id;

    /**
    * 用户角色名
    */
    @ApiModelProperty(value="用户角色名")
    private String userRoleName;

    /**
    * 用户权限组编码
    */
    @ApiModelProperty(value="用户权限组编码")
    private Long userRolePermissions;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
}