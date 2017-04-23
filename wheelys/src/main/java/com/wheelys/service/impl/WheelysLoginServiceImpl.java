package com.wheelys.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.ApiConstant;
import com.wheelys.model.acl.WheelysUser;
import com.wheelys.model.acl.User;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.untrans.CacheService;
import com.wheelys.untrans.monitor.MonitorService;
import com.wheelys.untrans.monitor.SysLogType;
import com.wheelys.util.DateUtil;
import com.wheelys.web.support.CachedAuthentication;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.helper.MemberUtils;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.WheelysLoginService;
import com.wheelys.untrans.UntransService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.support.MemberUpdaterTrigger;
import com.wheelys.web.support.token.MemberEncodeAuthenticationToken;

@Service("wheelysLoginService")
public class WheelysLoginServiceImpl extends BaseServiceImpl implements WheelysLoginService {

	private int allowIpNum = 6;	//允许IP变更的次数
	@Value("${member.checkPass}")
	private String checkPass = null;
	@Autowired@Qualifier("cacheService")
	private CacheService cacheService;
	@Autowired@Qualifier("monitorService")
	private MonitorService monitorService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("untransService")
	private UntransService untransService;
	
	@Override
	public ResultCode<MemberEncodeAuthenticationToken> getLogonMemberByMemberEncodeAndIp(String sessid, String remoteIp) {
		//缓存中获取用户信息
		MemberEncodeAuthenticationToken auth = (MemberEncodeAuthenticationToken) getBySessid(remoteIp, sessid);
		//获取不到执行登录操作
		if (auth == null && MemberUtils.isMemberEncode(sessid,checkPass)) {
			ResultCode<MemberEncodeAuthenticationToken> result = memberService.doLoginByMemberEncode(sessid, remoteIp, "encode");
			if(result.isSuccess()){
				auth = result.getRetval();
			}else{
				return ResultCode.getFailure(result.getMsg());
			}
		}
		if(auth == null) return ResultCode.getFailure("请登录！");
		return ResultCode.getSuccessReturn(auth);
	}

	/**
	 * 根据ip和SessionId或取用户ID
	 * 
	 * @param ip
	 * @param sessid
	 * @return
	 */
	private Authentication getBySessid(String ip, String sessid) {
		Authentication auth = loadAuthentication(ip, sessid);
		if(auth ==null ) return null;
		if (auth.getPrincipal() instanceof WheelysMember) {
			WheelysMember member = (WheelysMember) auth.getPrincipal();
			if (MemberUpdaterTrigger.isNeedRefresh(member)) {
				member = baseDao.getObject(WheelysMember.class, member.getId());
				if (StringUtils.equals(member.getRejected(), "Y")) {
					cacheService.remove(CacheService.REGION_LOGINAUTH, LoginUtils.getCacheUkey(sessid));
				} else {
					updateMemberAuth(sessid, member);
				}
			}
			return auth;
		}
		return null;
	}

	@Override
	public void updateMemberAuth(String encodeOrSessid, WheelysMember member) {
		if(StringUtils.isBlank(encodeOrSessid)){
			return;
		}
		String ukey = LoginUtils.getCacheUkey(encodeOrSessid);
		CachedAuthentication ca = (CachedAuthentication) cacheService.get(CacheService.REGION_LOGINAUTH, ukey);
		if (ca != null) {
			Authentication auth = ca.getAuthentication();
			if (auth instanceof UsernamePasswordAuthenticationToken 
			 || auth instanceof MemberEncodeAuthenticationToken) {
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
	public ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncodeAndSave(String username, String password,
			String remoteIp) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, "请输入登录信息！");
		}

		ResultCode code = untransService.checkLoginLimitNum(remoteIp);
		if (!code.isSuccess()) {
			Map<String, String> entry = new HashMap<String, String>();
			entry.put("ip", remoteIp);
			entry.put("username", username);
			entry.put("errortype", "ipLoginLimit");
			monitorService.addSysLog(SysLogType.userlogin, entry);
			return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, code.getMsg());
		}
		ResultCode<MemberEncodeAuthenticationToken> result = memberService.doLogin4MemberEncode(username, password, remoteIp, null, null);
		if (!result.isSuccess()) {
			ResultCode<String> mcode = untransService.updateLoginLimitInCache(remoteIp);
			if (!mcode.isSuccess()) {
				Map<String, String> entry = new HashMap<String, String>();
				entry.put("ip", remoteIp);
				entry.put("username", username);
				entry.put("errortype", "ipLoginLimit");
				monitorService.addSysLog(SysLogType.userlogin, entry);
				return ResultCode.getFailure(ApiConstant.CODE_DATA_ERROR, mcode.getMsg());
			}
			return ResultCode.getFailure(ApiConstant.CODE_MEMBER_NOT_EXISTS, result.getMsg());
		}
		return ResultCode.getSuccessReturn(result.getRetval());
	}

	public Authentication loadAuthentication(String ip, String sessid) {
		if(!LoginUtils.isValidSessid(sessid)) return null;
		String ukey = LoginUtils.getCacheUkey(sessid);
		CachedAuthentication ca =  (CachedAuthentication) cacheService.get(CacheService.REGION_LOGINAUTH, ukey);
		if(ca!=null) {
			if(!StringUtils.contains(ca.getIp(), ip)) {//IP变更
				Map entry = new HashMap();
				entry.put("oldip", ca.getIp());
				entry.put("newip", ip);
				entry.put("errortype", LoginUtils.ERROR_IPCHANGE);
				entry.put("username", ca.getAuthentication().getName());
				entry.put("usertype", ca.getAuthentication().getPrincipal().getClass().getName());
				Long memberid = ((WheelysUser)ca.getAuthentication().getPrincipal()).getId();
				monitorService.saveMemberLogMap(memberid, "login", entry, ip);
				
				String old = ca.getIp();
				if(StringUtils.contains(old, ',')){
					old = StringUtils.substringAfterLast(ca.getIp(), ",");
				}
				String nowcitycode = WebUtils.findCitycodeByIp();
				String oldcitycode = WebUtils.findCitycodeByIp();
				if(!StringUtils.equals(nowcitycode, oldcitycode)){
					dbLogger.warn("登录IP不匹配，" + ca.getAuthentication().getName() + ":" + old + "(" + oldcitycode + ")---->" + ip + "(" + nowcitycode + ")");
				}
				if(cannotChangeIp(ca.getAuthentication().getPrincipal())){
					return null;//不能更换ID
				}
				//TODO:同网段的IP不算更改计数
				if(StringUtils.split(ca.getIp(), ",").length >= allowIpNum ){//超过3个IP，直接forbidden，移出登录信息
					cacheService.remove(CacheService.REGION_LOGINAUTH, ukey);
					return null;
				}
				ca.setIp(ca.getIp() + "," + ip);
				cacheService.set(CacheService.REGION_LOGINAUTH, ukey, ca);
			}
			if(ca.getTimeout() != null && ca.getTimeout() < System.currentTimeMillis() + DateUtil.m_minute * 20){//20分钟即将超时，重新设置
				cacheService.set(CacheService.REGION_LOGINAUTH, ukey, ca);
			}
			return ca.getAuthentication();
		}
		return null;
	}
	
	private boolean cannotChangeIp(Object principal) {
		return principal != null && principal instanceof User ; 
	}

	@Override
	public void doLogout(String sessid, String remoteIp) {
		if(!LoginUtils.isValidSessid(sessid)) return;
		CachedAuthentication ca =  (CachedAuthentication) cacheService.get(CacheService.REGION_LOGINAUTH, LoginUtils.getCacheUkey(sessid));
		if(ca == null)return;
		WheelysMember member = (WheelysMember) ca.getAuthentication().getPrincipal();
		if(member != null){
			cacheService.remove(CacheService.REGION_LOGINAUTH, LoginUtils.getCacheUkey(sessid));
			addMemberLogoutLog(member.getId(), member.getNickname(), remoteIp);
		}
	}
	
	private void addMemberLogoutLog(Long memberid, String nickname, String ip){
		Map<String, String> changeMap = new HashMap<String, String>();
		changeMap.put("nickname", nickname);
		changeMap.put("errortype", LoginUtils.ERROR_LOGOUT);
		monitorService.saveMemberLogMap(memberid, "login", changeMap, ip);
	}

}
