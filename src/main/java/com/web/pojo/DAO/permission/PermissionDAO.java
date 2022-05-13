package com.web.pojo.DAO.permission;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
    * 用户权限表
    */
@ApiModel(value="用户权限表")
@Data
public class PermissionDAO {
    @ApiModelProperty(value="")
    private Integer id;

    /**
    * 用户权限码
    */
    @ApiModelProperty(value="用户权限码")
    private Long userPermissionCode;

    /**
    * 用户权限组名
    */
    @ApiModelProperty(value="用户权限组名")
    private String userPerimissionName;

    /**
    * 创建时间
    */
    @ApiModelProperty(value="创建时间")
    private Date createTime;
}