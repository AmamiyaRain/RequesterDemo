package com.web.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class CaptchaRequestEntity {

	private String id="62810fa4a36b69b0b7227405";

	private String secretkey="1a8d14d578c2435fba21e07139f20de0";

	private Integer scene=1;

	private String token;

	private String ip;

}
