package com.wheelys.web.action.acitvity;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.dao.Dao;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.StringUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.constant.MobileDynamicCodeConstant;
import com.wheelys.model.SMSRecord;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.mlink.SendMessageService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.util.OAuthUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class AcitvityController extends AnnotationController {
	
	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("sendMessageService")
	private SendMessageService sendMessageService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	
	@RequestMapping("/zhuanti/5yuan.xhtml")
	public String cafeShare(){
		return "/wps/zhuanti/cafeShare.vm";
	}
	
	@RequestMapping("/acitvity/wpshare.xhtml")
	public String toGetCoupon(ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"acitvity/wpshare.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		model.put("member", member);
		return "/acitvity/getCouponMuch.vm";
	}
	
	@RequestMapping("/acitvity/getCoupon.xhtml")
	public String getCoupon(String captcha, String mobile, ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		member = memberService.getWheelysMemberByMemberId(member.getId());
		if(!ValidateUtil.isMobile(member.getMobile())){
			ResultCode<WheelysMember> resultCode = memberService.bindMobile(member.getId(), mobile, captcha, WebUtils.getRemoteIp(request));
			if(resultCode.isSuccess()){
				member = resultCode.getRetval();
				//更新登录状态
				memberService.updateMemberAuth(LoginUtils.getSessid(request), member);
			}else{
				return this.showJsonError(model, resultCode.getMsg());
			}
		}
		int count = elecCardService.getBindElecCardCount(member, 238L, 0, DateUtil.getCurDateStr());
		if(count == 0){
			ResultCode result = elecCardService.bindElecCardByEbatchid(member, null, null, 238L, 1, DateUtil.getCurDateStr());
			if(!result.isSuccess()){
				return this.showJsonError(model, result.getMsg());
			}
			result = elecCardService.bindElecCardByEbatchid(member, null, null, 236L, 1, DateUtil.getCurDateStr());
			if(!result.isSuccess()){
				return this.showJsonError(model, result.getMsg());
			}
			result = elecCardService.bindElecCardByEbatchid(member, null, null, 237L, 1, DateUtil.getCurDateStr());
			if(!result.isSuccess()){
				return this.showJsonError(model, result.getMsg());
			}
			return this.showJsonSuccess(model, null);
		}
		return this.showJsonError(model, "您已经领取过！");
	}
	
	@RequestMapping("/acitvity/getCouponSuccess.xhtml")
	public String getCouponSuccess(ModelMap model){
		ElecCardBatch elecCardBatch1 = elecCardService.getElecCardBatch(238L, null);
		ElecCardBatch elecCardBatch2 = elecCardService.getElecCardBatch(236L, null);
		ElecCardBatch elecCardBatch3 = elecCardService.getElecCardBatch(237L, null);
		List<ElecCardBatch> batchList = new ArrayList<ElecCardBatch>();
		batchList.add(elecCardBatch1);
		batchList.add(elecCardBatch2);
		batchList.add(elecCardBatch3);
		model.put("batchList", batchList);
		return "/acitvity/getCouponResultMuch.vm";
	}
	
	@Autowired@Qualifier("baseDao")
	private Dao baseDao;
	private static final String MESSAGE = "嚯！我们又来送惊喜啦！“女神一号”红丝绒拿铁全新上线，更多饮品买一送一，还支持线上预约及外送服务~ 登录微信，点击WheelysCafe服务号，购买一杯好咖啡！【恒基名人店】"; 
	
	@RequestMapping("/activity/sendMessageByShopid.xhtml")
	public String sendMessageByShopid(String testmobile, Long shopid, String pwd, ModelMap model){
		if(!StringUtils.equals(pwd, StringUtil.md5(shopid + ""))){
			return showJsonError(model, "出错了哦");
		}
		List<String> result = new ArrayList<>();
		if(StringUtils.isNotBlank(testmobile)) {
			result = Arrays.asList(StringUtils.split(testmobile, ","));
		}else{
			String hql = "select mobile from WheelysMember where id in (select distinct(memberid) from WheelysOrder where shopid=? and paystatus = 'paid') and mobile is not null";
			result = baseDao.findByHql(hql, shopid);
		}
		
		dbLogger.warn("执行开始：" + result.size());
		long time = Clock.systemDefaultZone().millis();
		result.stream().forEach(mobile -> {
			SMSRecord sms = new SMSRecord(null, mobile, MESSAGE, Timestamp.valueOf(LocalDateTime.now()), MobileDynamicCodeConstant.VALID_MIN);
			sms.setTag("manual");
			ErrorCode resultCode = sendMessageService.sendMessage(sms);
			if(!resultCode.isSuccess()){
				dbLogger.error(mobile + ":" + resultCode.getMsg());
			}
		});
		dbLogger.warn("执行结束：" + (Clock.systemDefaultZone().millis() - time));
		return showJsonSuccess(model, null);
	}

}
