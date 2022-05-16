package com.web.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class SMSEntity {

	private String smsid="cb1a5f7d62914a2fbd6768ea29d9e3ec";

	private String smskey="531bff5d900f461e8ee0eb11060b9c43";

	private String token;

	private String[] data= {"_vcode"};

	private String countrycode="86";

	private String phone;

	private String templateid="1";


}
