package com.web.services.captcha;

import com.alibaba.fastjson.JSONObject;
import com.web.base.entity.CaptchaRequest;
import com.web.pojo.DTO.user.UserLoginDTO;

public interface CaptchaService {
	JSONObject getCaptchaValidationResult(String serverURL, CaptchaRequest captchaRequest);
}
