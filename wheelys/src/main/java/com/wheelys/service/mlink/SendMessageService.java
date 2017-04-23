package com.wheelys.service.mlink;

import com.wheelys.support.ErrorCode;
import com.wheelys.model.SMSRecord;

public interface SendMessageService {
	
	ErrorCode sendMessage(SMSRecord sms);

}
