package com.web.pojo.VO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UserLoginVO {
	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "电话")
	private String userTel;

	@ApiModelProperty(value = "邮箱")
	private String userEmail;

	@ApiModelProperty(value = "学号")
	private String userStuNo;

	@ApiModelProperty(value = "头像")
	private String userAvatar;

	@ApiModelProperty(value = "权限")
	private String userPermission;
}
