package com.web.services.captcha;

import com.alibaba.fastjson.JSONObject;
import com.web.base.entity.CaptchaRequestEntity;

public interface CaptchaService {
	JSONObject getCaptchaValidationResult(String serverURL, CaptchaRequestEntity captchaRequestEntity);
}
