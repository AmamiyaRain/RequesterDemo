package com.web.services.sms.impl;

import com.alibaba.fastjson.JSONObject;
import com.web.base.entity.SMSEntity;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.exceptions.BusinessException;
import com.web.services.sms.SMSService;
import com.web.util.http.HttpUtil;
import com.web.util.security.SyncUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SMSServiceImpl implements SMSService {
	@Override
	public void sendSMS(SMSEntity smsEntity) {
		if (SyncUtil.start(smsEntity)) {
			String result;
			try {
				result = HttpUtil.send("http://sms.vaptcha.com/send", (JSONObject) JSONObject.toJSON(smsEntity));
			} catch (IOException e) {
				throw new BusinessException(BusinessErrorEnum.CONNECTION_REQUEST_FAILED);
			}
			System.out.println(result);
			SyncUtil.finish(smsEntity);
		} else {
			throw new BusinessException(BusinessErrorEnum.REQUEST_IS_HANDLING);
		}
	}
}
