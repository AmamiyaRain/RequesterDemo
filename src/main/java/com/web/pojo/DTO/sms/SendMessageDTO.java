package com.web.pojo.DTO.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class SendMessageDTO {
	@ApiModelProperty(value = "Server",required = true)
	private String server;

	@ApiModelProperty(value = "用户手机号", required = true)
	private String userTel;

	@ApiModelProperty(value = "Token", required = true)
	private String token;
}
