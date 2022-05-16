package com.web.services.captcha.impl;

import com.alibaba.fastjson.JSONObject;
import com.web.base.entity.CaptchaRequestEntity;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.services.captcha.CaptchaService;
import com.web.util.http.HttpUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Override
	public JSONObject getCaptchaValidationResult(String serverURL, CaptchaRequestEntity captchaRequestEntity) {
		String data;
		try {
			data = HttpUtil.send(serverURL, (JSONObject) JSONObject.toJSON(captchaRequestEntity));
		} catch (IOException e) {
			throw new BusinessException(BusinessErrorEnum.CAPTCHA_CODE_ERROR);
		}
		return JSONObject.parseObject(data);
	}
}
