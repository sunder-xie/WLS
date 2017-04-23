package com.wheelys.service.impl;

import java.sql.Timestamp;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.cons.ApiConstant;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.constant.MobileDynamicCodeConstant;
import com.wheelys.constant.sys.LogTypeConstant;
import com.wheelys.helper.AccountHelper;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.model.user.WheelysMemberInfo;
import com.wheelys.service.MemberRegService;
import com.wheelys.service.MobileDynamicCodeService;

@Service("memberRegService")
public class MemberRegServiceImpl extends BaseServiceImpl implements MemberRegService {

	@Autowired@Qualifier("mobileDynamicCodeService")
	private MobileDynamicCodeService mobileDynamicCodeService;
	
	@Override
	public WheelysMember createMember(String source, String loginname, String ip) {
		String nickname =  source.substring(0,1) + StringUtil.getRandomString(10);
		WheelysMember member = new WheelysMember(nickname);
		String email = loginname;
		if(!ValidateUtil.isEmail(email)) {
			email = StringUtil.getRandomString(15) + "-open@" + source + ".com";
			WheelysMember exists = getMemberByEmail(email);
			if(exists!=null){
				email = StringUtil.getRandomString(15) + "-open@" + source + ".com";
			}
		}
		member.setPassword(StringUtil.md5(StringUtil.getRandomString(10)));
		member.setEmail(email);
		member.setNickname(nickname);
		Timestamp addtime = new Timestamp(System.currentTimeMillis());
		member.setAddtime(addtime);
		baseDao.saveObject(member);
		WheelysMemberInfo info = new WheelysMemberInfo(member, ip);
		info.setSource(source);
		return member;
	}
	
	@Override
	public OpenMember createOpenMember(WheelysMember member,String source,String loginname, String headimgurl, String nickname, String unionid) {
		OpenMember om = new OpenMember(member.getId(), source, loginname, member.getId());
		om.setOtherinfo(JsonUtils.addJsonKeyValue(om.getOtherinfo(), "rights","cafeorder"));
		om.setValidtime(DateUtil.addDay(DateUtil.getCurFullTimestamp(), 1080));
		om.setHeadpic(headimgurl);
		om.setNickname(nickname);
		if (StringUtils.isNotBlank(unionid)) {
			om.setUnionid(unionid);
		}
		baseDao.saveObject(om);
		return om;
	}
	
	private WheelysMember getMemberByEmail(String email) {
		WheelysMember member = this.baseDao.getObjectByUkey(WheelysMember.class, "email", email);
		return member;
	}
	
	@Override
	public ErrorCode<WheelysMember> dynamicCodeLoginOrRegister(String mobile, String checkpass, String source, String ip){
		if(!ValidateUtil.isMobile(mobile)) {
			return ErrorCode.getFailure("手机格式不正确！");
		}
		WheelysMember member = baseDao.getObjectByUkey(WheelysMember.class, "mobile", mobile);
		if(member != null) {
			ErrorCode bindMobileCode = mobileDynamicCodeService.checkDynamicCode(MobileDynamicCodeConstant.TAG_DYNAMICCODE, mobile, null, checkpass);
			if(!bindMobileCode.isSuccess()) {
				return ErrorCode.getFailure(bindMobileCode.getMsg());
			}
			return ErrorCode.getSuccessReturn(member);
		}

		String nickname = AccountHelper.getNicknameByMobile(mobile);
		String password = StringUtil.getRandomString(10);
		ErrorCode valid = validateRegInfo(nickname, password, password);
		if(!valid.isSuccess()){
			return valid;
		}
		ErrorCode bindMobileCode = mobileDynamicCodeService.checkDynamicCode(MobileDynamicCodeConstant.TAG_DYNAMICCODE, mobile, null, checkpass);
		if(!bindMobileCode.isSuccess()) {
			return ErrorCode.getFailure(bindMobileCode.getMsg());
		}
		ErrorCode<WheelysMember> code = createMember0(nickname, mobile, password, source, ip, "Y");
		return code;
	}
	private ErrorCode<WheelysMember> createMember0(String nickname, String mobileOrEmail, String password, String source, String ip, String bindStatus){
		Timestamp addtime = new Timestamp(System.currentTimeMillis());
		WheelysMember member = new WheelysMember(nickname);
		member.setAddtime(addtime);
		if(StringUtils.isNotBlank(ip)) member.setIp(ip);
		member.setPassword(StringUtil.md5(password));
		member.setSource(source);
		if(ValidateUtil.isMobile(mobileOrEmail)) {
			member.setMobile(mobileOrEmail);
			member.setBindStatus(bindStatus);
			baseDao.saveObject(member);
		}else {
			member.setEmail(mobileOrEmail);
			baseDao.saveObject(member);
		}
		
		//日志
		String msg = "手机号码用户注册成功";
		dbLogger.warnWithType(LogTypeConstant.LOG_TYPE_REGISTER, msg.toString() + member.getId());
		return ErrorCode.getSuccessReturn(member);
	}
	private ErrorCode validateRegInfo(String nickname, String password, String repassword) {
		ErrorCode passValid = ValidateUtil.validatePassword(password, repassword);
		if(!passValid.isSuccess()){
			return ErrorCode.getFailure(ApiConstant.CODE_DATA_ERROR, passValid.getMsg());
		}
		if(StringUtils.isBlank(nickname)){
			return ErrorCode.getFailure(ApiConstant.CODE_DATA_ERROR, "需要填写昵称！");
		}
		boolean matchNickname = ValidateUtil.isCNVariable(nickname, 1, 20);
		if(!matchNickname) {
			return ErrorCode.getFailure(ApiConstant.CODE_DATA_ERROR, "用户昵称格式不正确，不能包含特殊符号！");
		}
		return ErrorCode.SUCCESS;
	}

}
