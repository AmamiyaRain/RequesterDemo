package com.web.services.sms;

import com.web.base.entity.SMSEntity;

public interface SMSService {
	void sendSMS(SMSEntity smsEntity);
}
