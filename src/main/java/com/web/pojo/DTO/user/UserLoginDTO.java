package com.web.pojo.DTO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UserLoginDTO {

	@ApiModelProperty(value = "用户名", required = true)
	private String userName;

	@ApiModelProperty(value = "密码",required = true)
	private String userPassword;
}
