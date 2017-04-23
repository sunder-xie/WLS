package com.wheelys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.cons.MemberConstant;
import com.wheelys.helper.EncodeData;
import com.wheelys.helper.MemberUtils;
import com.wheelys.model.CafeShop;
import com.wheelys.model.MemberOrderInfo;
import com.wheelys.model.acl.User;
import com.wheelys.model.acl.WheelysUser;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberOrderInfoService;
import com.wheelys.service.MemberService;
import com.wheelys.service.cafe.OrderService;
import com.wheelys.untrans.CacheService;
import com.wheelys.untrans.monitor.MonitorService;
import com.wheelys.util.DateUtil;
import com.wheelys.util.PKCoderUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.web.action.admin.vo.WheelysMemberVo;
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
	@Qualifier("orderService")
	private OrderService orderService;
	@Autowired
	@Qualifier("memberOrderInfoService")
	private MemberOrderInfoService memberOrderInfoService;

	@Override
	public ResultCode<MemberEncodeAuthenticationToken> doLogin4MemberEncode(String username, String password,
			String remoteIp, String appkey, String srcsystem) {
		ResultCode<User> valid = isValidMemberPass(username, password, remoteIp);
		if (!valid.isSuccess()) {
			return ResultCode.getFailure(valid.getMsg());
		}
		User member = valid.getRetval();
		return doLoginByOpenMember(member, remoteIp, srcsystem);
	}

	@Override
	public ResultCode<MemberEncodeAuthenticationToken> doLoginByOpenMember(User member, String remoteIp,
			String srcsystem) {
		String memberEncode = MemberUtils.generateMemberEncode(member, checkPass);
		MemberEncodeAuthenticationToken auth = new MemberEncodeAuthenticationToken(member, memberEncode);
		auth.setAuthenticated(true);
		int duaration = cacheService.getCacheTime(CacheService.REGION_LOGINAUTH);
		this.saveAuthentication(remoteIp, duaration, false, auth);
		addLoginLog(member, remoteIp, memberEncode, "doLoginByOpenMember", srcsystem);
		return ResultCode.getSuccessReturn(auth);
	}

	private ResultCode<User> isValidMemberPass(String loginName, String plainPass, String ip) {
		User member = null;
		String errortype = LoginUtils.ERROR_PASSORNAME;
		if (ValidateUtil.isMobile(loginName)) {
			member = baseDao.getObjectByUkey(User.class, "username", loginName);
		} else {
			member = baseDao.getObjectByUkey(User.class, "username", loginName);
		}
		if (member != null) {
			String encodePass = StringUtil.md5(plainPass);
			if (StringUtils.equals(member.getPassword(), encodePass)) {
				if (member.isEnabled()) {
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
				User member = encodeData.getMember();
				if (newtime <= valtime) {// 有效期内
					if (!member.isEnabled()) {
						return ResultCode.getFailure("用户被禁用，请联系客服！");
					}
					if (StringUtils.equals(member.getPassword(), MemberConstant.RESET_PASSWORD)) {
						return ResultCode.getFailure(MemberConstant.RESET_PASS_ERROR_MSG);
					}
					if (StringUtils.equals(encodeData.getPassvalid(), "Y")) {// 验证合法
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
					dbLogger.error("重复用户登录，剔除用户" + repeatKey + " " + user.getUsername() + ",rememberMe" + rememberMe);
				}
				cacheService.set(CacheService.REGION_LOGINAUTH, repeatKey, sessid);
			}
			return sessid;
		} else {
			throw new IllegalStateException(user.getUsername() + " not authenticated!");
		}
	}

	private EncodeData getMemberEncodeData(String memberEncode, boolean doValidate) {
		String[] pair = memberEncode.split("@");
		if (pair.length > 1) {
			if (StringUtil.md5(pair[0] + checkPass).substring(0, 8).equalsIgnoreCase(pair[1])) {
				String data = PKCoderUtil.decryptString(pair[0], checkPass);
				Long memberid = new Long(data.substring(11, data.length() - 6));
				Long valtime = new Long(StringUtils.substring(data, data.length() - 6));
				String passvalid = "U";// 密码未验证
				User member = null;
				if (doValidate) {
					member = baseDao.getObject(User.class, memberid);
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

	private void addLoginLog(User member, String ip, String sessid, String loginBy, String srcsystem) {
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
	public List<WheelysMemberVo> findMemberWithLatestOrder(Integer pageNo, int rowPerPage, String region,
			String cityCode, Long shopId, String phone, Date paidTimeBegin, Date paidTimeEnd) {
		// 获取用户最新的订单的信息
		List<MemberOrderInfo> orderDeatails = memberOrderInfoService.getMemberLatestOrderInfo(pageNo, rowPerPage,
				region, cityCode, shopId, phone, paidTimeBegin, paidTimeEnd);
		List<WheelysMemberVo> result = new ArrayList<WheelysMemberVo>();
		if (CollectionUtils.isNotEmpty(orderDeatails)) {
			for (MemberOrderInfo memberOrderInfo : orderDeatails) {
				WheelysMember wheelysMember = baseDao.getObject(WheelysMember.class, memberOrderInfo.getMemberid());
				WheelysMemberVo vo = new WheelysMemberVo();
				if (wheelysMember != null) {
					BeanUtils.copyProperties(wheelysMember, vo);
				}
				vo.setId(memberOrderInfo.getMemberid());
				vo.setTotalFee(memberOrderInfo.getTotalnetpaid());
				vo.setAddtime(memberOrderInfo.getRegstertime());
				vo.setTotalOrderQuantity(memberOrderInfo.getOrdernum());
				vo.setLastConsumerTime(memberOrderInfo.getLastordertime());
				vo.setLatestTradeno(memberOrderInfo.getLasttradeno());// 最后一次下单的订单号
				CafeShop cafeShop = baseDao.getObject(CafeShop.class, memberOrderInfo.getLastshopid()); // 查询最后一次下单的店铺
				vo.setLastConsumerShop(cafeShop == null ? null : cafeShop.getShopname());
				vo.setCityCode(cafeShop == null ? null : cafeShop.getCitycode());
				result.add(vo);
			}
		}
		return result;
	}
}
