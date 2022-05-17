package com.web.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class CaptchaRequestEntity {

	private String id="urID";

	private String secretkey="urSecretKey";

	private Integer scene=1;

	private String token;

	private String ip;

}
