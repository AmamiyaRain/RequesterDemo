package com.web.base.entity;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Data
public class CaptchaResult {
	private Integer success;

	private Integer score;

	private String msg;
}
