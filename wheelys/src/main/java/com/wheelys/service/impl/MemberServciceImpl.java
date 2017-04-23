package com.wheelys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.ApiConstant;
import com.wheelys.cons.MemberConstant;
import com.wheelys.helper.EncodeData;
import com.wheelys.helper.MemberUtils;
import com.wheelys.model.acl.WheelysUser;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.MobileDynamicCodeService;
import com.wheelys.support.ErrorCode;
import com.wheelys.untrans.CacheService;
import com.wheelys.untrans.monitor.MonitorService;
import com.wheelys.util.DateUtil;
import com.wheelys.util.PKCoderUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.util.WebUtils;
import com.wheelys.web.support.CachedAuthentication;
import com.wheelys.web.support.InvalidBizException;
import com.wheelys.web.support.ShSessidGenerator;
import com.wheelys.web.support.auth.SessidGenerator;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;
import com.wheelys.web.util.LoginUtils;

@Service("memberService")
public class MemberServciceImpl extends BaseServiceImpl implements MemberService {

	@Value("${member.checkPass}")
	private String checkPass = null;
	private Boolean isRelogin = false;
	protected SessidGenerator sessidGenerator = new ShSessidGenerator();
	@Autowired
	@Qualifier("cacheService")
	private CacheService cacheService;
	@Autowired
	@Qualifier("monitorService")
	private MonitorService monitorService;
	@Autowired
	@Qualifier("mobileDynamicCodeService")
	private MobileDynamicCodeService mobileDynamicCodeService;

	@Override
	public ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncode(String username, String password,
			String remoteIp, String appkey, String srcsystem) {
		ResultCode<WheelysMember> valid = isValidMemberPass(username, password, remoteIp);
		if (!valid.isSuccess()) {
			return ResultCode.getFailure(valid.getMsg());
		}
		WheelysMember member = valid.getRetval();
		return doLoginByOpenMember(member, remoteIp, srcsystem);
	}

	@Override
	public ResultCode<MemberEncodeAuthenticationToken> doLoginByOpenMember(WheelysMember member, String remoteIp,
			String srcsystem) {
		String memberEncode = MemberUtils.generateMemberEncode(member, checkPass);
		MemberEncodeAuthenticationToken auth = new MemberEncodeAuthenticationToken(member, memberEncode);
		auth.setAuthenticated(true);
		int duaration = cacheService.getCacheTime(CacheService.REGION_LOGINAUTH);
		this.saveAuthentication(remoteIp, duaration, false, auth);
		addLoginLog(member, remoteIp, memberEncode, "doLoginByOpenMember", srcsystem);
		return ResultCode.getSuccessReturn(auth);
	}

	private ResultCode<WheelysMember> isValidMemberPass(String loginName, String plainPass, String ip) {
		WheelysMember member = null;
		String errortype = LoginUtils.ERROR_PASSORNAME;
		if (ValidateUtil.isMobile(loginName)) {
			member = baseDao.getObjectByUkey(WheelysMember.class, "mobile", loginName);
		} else {
			member = baseDao.getObjectByUkey(WheelysMember.class, "email", loginName);
		}
		if (member != null) {
			String encodePass = StringUtil.md5(plainPass);
			if (StringUtils.equals(member.getPassword(), encodePass)) {
				if (member.isEnabled()) {
					member.setLastLoginTime(System.currentTimeMillis());// 用户最新登录时间
					return ResultCode.getSuccessReturn(member);
				} else {
					return ResultCode.getFailure("用户被禁用，请联系客服！");
				}
			} else {
				if (StringUtils.equals(member.getPassword(), MemberConstant.RESET_PASSWORD)) {
					return ResultCode.getFailure(MemberConstant.RESET_PASS_ERROR_MSG);
				}
				errortype = LoginUtils.ERROR_PASSWORD;
			}
		} else {
			errortype = LoginUtils.ERROR_USERNAME;
		}
		// 记录日志
		Map<String, String> result = new HashMap();
		result.put("membername", loginName);
		result.put("errortype", errortype);
		monitorService.saveMemberLogByName(loginName, "login", result, ip);
		return ResultCode.getFailure("用户名或密码错误！");
	}

	@Override
	public ResultCode<MemberEncodeAuthenticationToken> doLoginByMemberEncode(String memberEncode, String remoteIp,
			String srcsystem) {
		try {
			EncodeData encodeData = getMemberEncodeData(memberEncode, true);
			if (encodeData != null) {
				Long memberid = encodeData.getMemberid();
				Long valtime = encodeData.getValidMonth();
				Long newtime = new Long(DateUtil.format(new Date(System.currentTimeMillis()), "yyyyMM"));
				WheelysMember member = encodeData.getMember();
				if (newtime <= valtime) {// 有效期内
					if (!member.isEnabled()) {
						return ResultCode.getFailure("用户被禁用，请联系客服！");
					}
					if (StringUtils.equals(member.getPassword(), MemberConstant.RESET_PASSWORD)) {
						return ResultCode.getFailure(MemberConstant.RESET_PASS_ERROR_MSG);
					}
					if (StringUtils.equals(encodeData.getPassvalid(), "Y")) {// 验证合法
						member.setLastLoginTime(System.currentTimeMillis());// 用户最新登录时间

						MemberEncodeAuthenticationToken auth = new MemberEncodeAuthenticationToken(member,
								memberEncode);
						auth.setAuthenticated(true);
						int duaration = cacheService.getCacheTime(CacheService.REGION_LOGINAUTH);
						this.saveAuthentication(remoteIp, duaration, false, auth);
						addLoginLog(member, remoteIp, memberEncode, "encode", srcsystem);
						return ResultCode.getSuccessReturn(auth);
					} else {// 密码已经修改
						dbLogger.warn("memberEncode用户登录错误：" + memberid);
						return ResultCode.getFailure("密码错误，请重新登录！");
					}
				} else {
					return ResultCode.getFailure("登录信息过期，请重新登录！");
				}
			} else {
				return ResultCode.getFailure("登录信息有误，请重新登录！");
			}
		} catch (Exception e) {
			dbLogger.warn("memberEncode用户登录错误：", e);
			return ResultCode.getFailure("登录错误，请重新登录！");
		}
	}

	private String saveAuthentication(String ip, int duration, boolean rememberMe, Authentication authentication) {
		if (StringUtils.isBlank(ip)) {// IP 不允许为空
			throw new InvalidBizException("login ip can not be null!");
		}
		WheelysUser user = (WheelysUser) authentication.getPrincipal();
		if (authentication.isAuthenticated()) {
			String sessid = sessidGenerator.generateSessid(authentication);

			CachedAuthentication ca = new CachedAuthentication();
			ca.setAuthentication(authentication);
			ca.setIp(ip);
			ca.setTimeout(System.currentTimeMillis() + duration * 1000);
			String ukey = LoginUtils.getCacheUkey(sessid);
			cacheService.set(CacheService.REGION_LOGINAUTH, ukey, ca, duration);

			String repeatKey = LoginUtils.getRepeatKey(user.getUsertype(), user.getUsername());
			if (isRelogin) {
				Object sessidOld = cacheService.get(CacheService.REGION_LOGINAUTH, repeatKey);
				if (sessidOld != null && !sessid.equals(sessidOld)) {
					cacheService.remove(CacheService.REGION_LOGINAUTH, LoginUtils.getCacheUkey(sessidOld.toString()));
					dbLogger.error("重复用户登录，剔除用户" + repeatKey + " " + user.getUsername());
				}
				cacheService.set(CacheService.REGION_LOGINAUTH, repeatKey, sessid);
			}
			return sessid;
		} else {
			throw new IllegalStateException(user.getUsername() + " not authenticated!" + rememberMe);
		}
	}

	@Override
	public EncodeData getMemberEncodeData(String memberEncode, boolean doValidate) {
		String[] pair = memberEncode.split("@");
		if (pair.length > 1) {
			if (StringUtil.md5(pair[0] + checkPass).substring(0, 8).equalsIgnoreCase(pair[1])) {
				String data = PKCoderUtil.decryptString(pair[0], checkPass);
				Long memberid = new Long(data.substring(11, data.length() - 6));
				Long valtime = new Long(StringUtils.substring(data, data.length() - 6));
				String passvalid = "U";// 密码未验证
				WheelysMember member = null;
				if (doValidate) {
					member = baseDao.getObject(WheelysMember.class, memberid);
					String pass = StringUtils.substring(data, 3, 11);
					String mypass = StringUtil.md5(member.getPassword() + checkPass).substring(8, 16);
					if (StringUtils.equals(pass, mypass)) {
						passvalid = "Y";
					} else {
						passvalid = "N";
					}
				}
				return new EncodeData(memberid, valtime, passvalid, member);
			}
		}
		return null;
	}

	private void addLoginLog(WheelysMember member, String ip, String sessid, String loginBy, String srcsystem) {
		String nickname = member.getRealname();
		Map<String, String> info = new HashMap<String, String>();
		info.put("nickname", nickname);
		info.put("sid", StringUtil.md5(sessid));
		info.put("loginBy", loginBy);
		if (StringUtils.isNotBlank(srcsystem)) {
			info.put("srcsystem", srcsystem);
		}
		monitorService.saveMemberLogMap(member.getId(), "login", info, ip);
	}

	@Override
	public OpenMember getOpenMemberByLoginname(String sourceWeixin, String unionid) {
		OpenMember member = baseDao.getObjectByUkey(OpenMember.class, "loginname", unionid);
		return member;
	}

	@Override
	public OpenMember getOpenMemberByMemberid(Long memberid) {
		OpenMember member = baseDao.getObjectByUkey(OpenMember.class, "memberid", memberid);
		return member;
	}

	@Override
	public void updateOpenMemberInfo(Long memberid, String nickname, String headimg) {
		OpenMember openMember = getOpenMemberByMemberid(memberid);
		openMember.setHeadpic(headimg);
		openMember.setNickname(WebUtils.removeFourChar(nickname));
		this.baseDao.saveObject(openMember);
	}

	@Override
	public WheelysMember getWheelysMemberByMemberId(Long memberid) {
		return this.baseDao.getObject(WheelysMember.class, memberid);
	}

	@Override
	public ResultCode<WheelysMember> bindMobile(Long memberid, String bindMobile, String checkpass, String remoteIp) {
		return bindMobileInternal(memberid, bindMobile, checkpass, remoteIp, true);
	}

	@Override
	public ResultCode<WheelysMember> changeMobile(Long memberid, String bindMobile, String checkpass, String remoteIp) {
		return bindMobileInternal(memberid, bindMobile, checkpass, remoteIp, false);
	}

	private ResultCode<WheelysMember> bindMobileInternal(Long memberid, String bindMobile, String checkpass,
			String remoteIp, boolean isInitBind) {
		WheelysMember member = baseDao.getObject(WheelysMember.class, memberid);
		if (member == null) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, "用户不存在！");
		}
		String oldMobile = member.getMobile();
		if (!ValidateUtil.isMobile(bindMobile)) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, "手机号输入错误！");
		}

		// 绑定手机用户，此处更改绑定手机号需与原手机不同
		if (member.isBindMobile() && bindMobile.equals(oldMobile)) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, "该手机号码已经绑定过账户，请更换新的手机号码！");
		}
		if (member.isBindMobile() && isInitBind) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, "该账号已绑定过手机号！");
		}
		String tag = MemberConstant.TAG_BINDMOBILE;
		if (StringUtils.isNotBlank(oldMobile)) {
			tag = MemberConstant.TAG_CHANGEMOBILE;
		}
		// 此处校验动态码是否正确及时效
		ErrorCode checkCode = mobileDynamicCodeService.checkDynamicCode(tag, bindMobile, member.getId(), checkpass);
		if (!checkCode.isSuccess()) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, checkCode.getMsg());
		}
		// 非待验证用户，待验证手机号与待绑定手机号一致，此处毋须验证
		WheelysMember bindmobileMember = getWheelysMemberByMobile(bindMobile);
		if (bindmobileMember != null) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, "该手机号已绑定其他用户！");
		}
		// 绑定到用户
		member.setMobile(bindMobile);
		member.setBindStatus(MemberConstant.BINDMOBILE_STATUS_Y);
		if (member.getNeedValid() == null) {// 更新兼容
			member.setNeedValid(MemberConstant.NEEDVALID_U);
		}
		baseDao.saveObject(member);
		Map<String, String> logInfo = new HashMap<String, String>();
		if (StringUtils.isNotBlank(oldMobile)) {
			logInfo.put("oldMobile", oldMobile);
			logInfo.put("newMobile", bindMobile);
		}
		monitorService.saveMemberLogMap(member.getId(), MemberConstant.TAG_BINDMOBILE, logInfo, remoteIp);
		return ResultCode.getSuccessReturn(member);
	}

	@Override
	public WheelysMember getWheelysMemberByMobile(String mobile) {
		WheelysMember member = baseDao.getObjectByUkey(WheelysMember.class, "mobile", mobile);
		return member;
	}

	@Override
	public void updateMemberAuth(String encodeOrSessid, WheelysMember member) {
		if (StringUtils.isBlank(encodeOrSessid)) {
			return;
		}
		String ukey = LoginUtils.getCacheUkey(encodeOrSessid);
		CachedAuthentication ca = (CachedAuthentication) cacheService.get(CacheService.REGION_LOGINAUTH, ukey);
		if (ca != null) {
			Authentication auth = ca.getAuthentication();
			if (auth instanceof MemberEncodeAuthenticationToken) {
				WheelysMember old = (WheelysMember) auth.getPrincipal();
				old.setMobile(member.getMobile());
				old.setPassword(member.getPassword());
				old.setEmail(member.getEmail());
				old.setNickname(member.getNickname());
				old.setBindStatus(member.getBindStatus());
				cacheService.set(CacheService.REGION_LOGINAUTH, ukey, ca);
			}
		}
	}

	@Override
	public WheelysMember getWheelysMemberByMemberencode(String memberencode) {
		// TODO Auto-generated method stub
		return null;
	}

}
