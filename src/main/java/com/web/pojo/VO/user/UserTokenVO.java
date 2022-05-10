package com.web.pojo.VO.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class UserTokenVO {
	@ApiModelProperty(value = "用户Token")
	private String userToken;
}
