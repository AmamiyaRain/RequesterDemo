package com.web.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class SMSEntity {

	private String smsid="urSmsId";

	private String smskey="urSmsKey";

	private String token;

	private String[] data= {"_vcode"};

	private String countrycode="86";

	private String phone;

	private String templateid="1";


}
