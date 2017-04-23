package com.wheelys.web.action.wap;

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
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.constant.MobileDynamicCodeConstant;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.MobileDynamicCodeService;
import com.wheelys.service.mlink.SendMessageService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.util.OAuthUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class ElecCardController extends AnnotationController  {

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("sendMessageService")
	private SendMessageService sendMessageService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("mobileDynamicCodeService")
	private MobileDynamicCodeService mobileDynamicCodeService;
	
	@RequestMapping("/hongbao/wpshare.xhtml")
	public String toGetCoupon(String ukey, ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"hongbao/wpshare.xhtml?ukey="+ukey;
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		model.put("member", member);
		ElecCardBatch elecCardBatch = elecCardService.getElecCardBatch(null, ukey);
		Long ebatchid = elecCardBatch.getId();
		model.put("elecCardBatch", elecCardBatch);
		if(ebatchid != null && ebatchid.intValue() == 241){
			return "/acitvity/getCouponMuch2.vm";
		}
		return "/wps/home/getCoupon.vm";
	}
	
	@RequestMapping("/hongbao/sendmsg.xhtml")
	public String sendmsg(String mobile, HttpServletRequest request, ModelMap model){
		WheelysMember member = this.getLogonMember();
		if(member == null) return showJsonError(model, "请先登录！");
		if(member.isBindMobile()) return showJsonError(model, "已绑定手机号！");
		if(!ValidateUtil.isMobile(mobile)) return showJsonError(model, "手机格式有误！");
		ErrorCode resultCode = mobileDynamicCodeService.sendDynamicCode(MobileDynamicCodeConstant.TAG_BINDMOBILE, mobile, member.getId(), WebUtils.getRemoteIp(request));
		if(!resultCode.isSuccess()){
			return showJsonError(model, resultCode.getMsg());
		}
		return this.showJsonSuccess(model, null);
	}
	private static String mobiles = "13601940969,15000440808,13916986617,13795292914,18516765224,13916467175,15850612832,15800700086,18616570613,13482228484,13564499770,13601940369,18906765559,13764504418,17797174779,13916988825,13817804090,18616214370,13917523604,18817823019,13817245471,13472770248,15021732567,18916224979,18516561021,15212284460,13524088175,15900719249,13585841520,18221301620,15026577391,15665593969,15821317412,13564387967,15800653200,15801801908,15618060926,13818330274,18101936616,15221010659,18601634536,13636371320,13636324112,13482522929,13918170817,13761963393";
	@RequestMapping("/home/getCoupon.xhtml")
	public String getCoupon(String ukey, String cardkey, String sign, String captcha, String mobile, ModelMap model, HttpServletRequest request){
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
		ElecCardBatch elecCardBatch = elecCardService.getElecCardBatch(null, ukey);
		String key = null;
		if(elecCardBatch.getCycleday() != null && elecCardBatch.getCycleday() > 0){
			if(elecCardBatch.getCycleday() == 1){
				key = DateUtil.getCurDateStr();
			}
		}
		Long ebatchid = elecCardBatch.getId();
		int count = elecCardService.getBindElecCardCount(member, ebatchid, 0, key);
		if(StringUtils.equals(elecCardBatch.getCardtype(), ElecCardConstant.CARDTYPE_EXCHANGE) && StringUtils.isBlank(ukey)){
			return this.showJsonError(model, "不能领取！");
		}
		List<String> mobileList = Arrays.asList(StringUtils.split(mobiles, ","));
		if(StringUtils.equals("4e2982a9427d156efacccd2e7cb7b0e6", ukey) && !mobileList.contains(member.getMobile())){
			return this.showJsonError(model, "不能领取！");
		}
		if(count == 0){
			int receivenum = 1;
			if(elecCardBatch.getReceivenum() != null && elecCardBatch.getReceivenum() > 0){
				receivenum = elecCardBatch.getReceivenum();
			}
			ResultCode result = elecCardService.bindElecCardByEbatchid(member, cardkey, sign, ebatchid, receivenum, key);
			if(!result.isSuccess()){
				return this.showJsonError(model, result.getMsg());
			}
			return this.showJsonSuccess(model, null);
		}
		return this.showJsonError(model, "您已经领取过！");
	}
	
	@RequestMapping("/home/getCouponSuccess.xhtml")
	public String getCouponSuccess(String ukey, ModelMap model){
		ElecCardBatch elecCardBatch = elecCardService.getElecCardBatch(null, ukey);
		model.put("elecCardBatch", elecCardBatch);
		if(elecCardBatch.getDaynum() > 0){
			model.put("begintime", DateUtil.getMillTimestamp());
			model.put("endtime", DateUtil.addDay(DateUtil.getMillTimestamp(), elecCardBatch.getDaynum()));
		}
		Long ebatchid = elecCardBatch.getId();
		if(ebatchid != null && ebatchid.intValue() == 241){
			return "/acitvity/getCouponResultMuch2.vm";
		}
		return "/wps/home/getCouponResult.vm";
	}
	
	@RequestMapping("/card/instructions.xhtml")
	public String instructions(){
		return "/wps/home/instructions.vm";
	}

}
