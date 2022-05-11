package com.web.pojo.DTO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UserModifyPasswordDTO {

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "原密码")
	private String userPassword;

	@ApiModelProperty(value = "新密码")
	private String updatedUserPassword;

}
