package com.wheelys.service.impl;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.cons.ApiConstant;
import com.wheelys.dao.Dao;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.constant.MobileDynamicCodeConstant;
import com.wheelys.constant.OperationConstant;
import com.wheelys.model.SMSRecord;
import com.wheelys.model.user.MobileDynamicCode;
import com.wheelys.service.MobileDynamicCodeService;
import com.wheelys.service.OperationService;
import com.wheelys.service.mlink.SendMessageService;
import com.wheelys.util.VmUtils;

@Service("mobileDynamicCodeService")
public class MobileDynamicCodeServiceImpl implements MobileDynamicCodeService {
	
	@Autowired@Qualifier("operationService")
	private OperationService operationService;
	@Autowired@Qualifier("baseDao")
	private Dao baseDao;
	@Autowired@Qualifier("sendMessageService")
	private SendMessageService sendMessageService;
	
	@Override
	public ErrorCode sendDynamicCode(String tag, String mobile, Long memberid, String ip) {
		String msgTemplate = MobileDynamicCodeConstant.getMsgTemplate(tag);
		return sendDynamicCodeInternal(tag, mobile, memberid, ip, msgTemplate, true);
	}

	@Override
	public ErrorCode checkDynamicCode(String tag, String mobile, Long memberid, String checkpass) {
		return checkDynamicCodeMobile(tag, mobile, checkpass, memberid, 500);
	}

	private ErrorCode sendDynamicCodeInternal(String tag, String mobile, Long memberid, String ip, String msgTemplate, final boolean checkSecurity){
		if(!ValidateUtil.isMobile(mobile)) return ErrorCode.getFailure("手机号码格式不正确！");
		if(!MobileDynamicCodeConstant.VALID_TAG_LIST.contains(tag)) throw new IllegalArgumentException("类型不正确！");
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		String checkpass = StringUtil.getDigitalRandomString(4);
		Timestamp validtime = DateUtil.addMinute(curtime, MobileDynamicCodeConstant.VALID_MIN);
		MobileDynamicCode dynamicCode = baseDao.getObject(MobileDynamicCode.class, tag + mobile);
		//1、ip检查
		/*
		if(checkSecurity && !StringUtils.equals(tag, BindConstant.TAG_DYNAMICCODE) && bind!=null && bind.getSendcount() >= BindConstant.getMaxSendnum(tag)){
			dbLogger.warn("您的手机号有异常，请联系客服解决：" + tag + ", " + mobile);
			return ErrorCode.getFailure("您的手机号有异常，请联系客服解决！");
		}*/
		//一个ip1小时获取100次，基站会导致大部分用户ip一样
		String opkey = tag + ip;
		boolean allow = operationService.updateOperation(opkey, 10, OperationConstant.ONE_HOUR, 100);
		if(checkSecurity && !allow){
			return ErrorCode.getFailure(ApiConstant.CODE_DATA_ERROR, "您的IP获取动态码太过频繁！");
		}
		//一个手机号1分钟获取1次动态码
		String opMkey = "more" + tag + mobile;
		allow = operationService.updateOperation(opMkey, OperationConstant.ONE_MINUTE, 1);
		if(checkSecurity && !allow){
			return ErrorCode.getFailure(ApiConstant.CODE_DATA_ERROR, "获取过于频繁，请稍后获取！");
		}
		//2、手机号检查，每天最多发10次
		String mobileKey = tag + mobile;
		allow = operationService.updateOperation(mobileKey, OperationConstant.ONE_DAY, 10);
		if(checkSecurity && !allow){
			return ErrorCode.getFailure(ApiConstant.CODE_DATA_ERROR, "获取动态码次数过多！");
		}
		if(dynamicCode==null){
			dynamicCode = new MobileDynamicCode(tag, mobile, checkpass, ip);
		}else{
			if(dynamicCode.getCheckcount()>=1 || curtime.after(dynamicCode.getValidtime())){//换密码
				dynamicCode.setCheckpass(checkpass);
			}
			dynamicCode.setLastip(ip);
			dynamicCode.setCheckcount(0);			//复位
			dynamicCode.setMobile(mobile);
		}
		dynamicCode.setMemberid(memberid);
		dynamicCode.setValidtime(validtime);
		dynamicCode.setSendcount(dynamicCode.getSendcount() + 1);
		
		String msg = StringUtils.replace(msgTemplate, "checkpass", dynamicCode.getCheckpass());
		SMSRecord sms = new SMSRecord(memberid, mobile, msg, curtime, MobileDynamicCodeConstant.VALID_MIN);
		//SMSRecord sms = new SMSRecord(null, ukey, mobile, msg, curtime, DateUtil.addMinute(curtime, 5), SmsConstant.SMSTYPE_DYNCODE);
		sms.setTag(tag);
		ErrorCode resultCode = sendMessageService.sendMessage(sms);
		if(!resultCode.isSuccess()){
			return ErrorCode.getFailure(resultCode.getMsg());
		}
		baseDao.saveObject(dynamicCode);
		return ErrorCode.SUCCESS;
	}
	private ErrorCode checkDynamicCodeMobile(String tag, String mobile, String checkpass, Long memberid, int successIncrease){
		if(!ValidateUtil.isMobile(mobile)) return ErrorCode.getFailure("手机号码格式不正确！");
		if(StringUtils.isBlank(checkpass)) return ErrorCode.getFailure("动态码不能为空！");
		Timestamp curtime = DateUtil.getCurFullTimestamp();
		MobileDynamicCode dynamicCode = baseDao.getObject(MobileDynamicCode.class, tag + mobile);
		if(dynamicCode == null) return ErrorCode.getFailure("动态码错误，请重新获取！");
		if (curtime.after(dynamicCode.getValidtime()) || dynamicCode.getCheckcount() > MobileDynamicCodeConstant.VALID_MAXCHECK) {
			return ErrorCode.getFailure("动态码失效，请重新获取！");
		}
		dynamicCode.setTotalcheck(dynamicCode.getTotalcheck() + 1);
		boolean validMember = VmUtils.eq(memberid, dynamicCode.getMemberid());
		if(validMember && StringUtils.equalsIgnoreCase(dynamicCode.getCheckpass(), checkpass)){
			dynamicCode.setCheckcount(dynamicCode.getCheckcount() + 1);
			dynamicCode.setCheckcount(dynamicCode.getCheckcount() + successIncrease);
			baseDao.saveObject(dynamicCode);
			return ErrorCode.getSuccessReturn(dynamicCode); 
		}else{
			if(!validMember){
				return ErrorCode.getFailure("无效的动态码，请重新获取！！");
			}
			dynamicCode.setCheckcount(dynamicCode.getCheckcount() + 1);
			baseDao.saveObject(dynamicCode);
			return ErrorCode.getFailure("动态码错误，请重新输入！");
		}
	}
}
