package com.wheelys.service.mlink.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.SMSRecord;
import com.wheelys.service.mlink.SendMessageService;

@Service("sendMessageService")
public class SendMessageServiceImpl extends BaseServiceImpl implements SendMessageService {

	public static final List<String> ALLMOBILE = Arrays.asList("17770035436","18516561021","15021732567","13472770248","");
	@Value("${mlink.spid}")
	private String spid;		//
	@Value("${mlink.sppassword}")
	private String sppassword;	//
	@Override
	public ErrorCode sendMessage(SMSRecord sms){
		if(sms == null) return ErrorCode.getFailure("sms为null");
		ResultCode reuslt = SendMessageUtils.sendMessage(sms, spid, sppassword);
		if(reuslt.isSuccess()){
			sms.setStatus("Y");
		}else{
			sms.setStatus("N");
			dbLogger.warn("发送短信错误：" + JsonUtils.writeObjectToJson(sms) + " errorMsg:" + reuslt.getMsg());
		}
		this.baseDao.saveObject(sms);
		if(StringUtils.equals(sms.getStatus(), "Y")){
			return ErrorCode.SUCCESS;
		}else{
			return ErrorCode.getFailure("短信发送失败。");
		}
	}
}
