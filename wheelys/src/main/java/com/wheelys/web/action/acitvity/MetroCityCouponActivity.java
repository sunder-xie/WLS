package com.wheelys.web.action.acitvity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.Config;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.ValidateUtil;
import com.wheelys.web.util.LoginUtils;
import com.wheelys.model.order.CardCouponsOrder;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.cafe.CafeShopProfileService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.mlink.SendMessageService;
import com.wheelys.service.order.CardCouponsOrderService;
import com.wheelys.service.order.MemberAddressService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.untrans.pay.WxMpPayService;
import com.wheelys.util.OAuthUtils;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class MetroCityCouponActivity extends AnnotationController{
	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("memberAddressService")
	private MemberAddressService memberAddressService;
	@Autowired@Qualifier("cafeShopProfileService")
	private CafeShopProfileService cafeShopProfileService;
	@Autowired@Qualifier("wxMpPayService")
	private WxMpPayService wxMpPayService;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("sendMessageService")
	private SendMessageService sendMessageService;
	@Autowired@Qualifier("cardCouponsOrderService")
	private CardCouponsOrderService cardCouponsOrderService;
	
	@RequestMapping("/activity/metroCity.xhtml")
	public String valentine(ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"activity/metroCity.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		model.put("member", member);
		return "/acitvity/getCouponMuch3.vm";
	}
	
	@RequestMapping("/activity/metroCityTopay.xhtml")
	public String pay(String captcha, String mobile, String datetype0, String datetype1, ModelMap model, HttpServletRequest request, HttpServletResponse response){
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
		String citycode = WebUtils.getAndSetCityCode(request, response);
		String ukey = "";
		OpenMember openMember = memberService.getOpenMemberByMemberid(member.getId());
		if(openMember != null){
			ukey = openMember.getLoginname();
		}
		ResultCode<CardCouponsOrder> result = cardCouponsOrderService.createValentineOrder(member, 56L, ukey, citycode, datetype1, "美罗城活动",datetype0);
		if(result.isSuccess()){
			String clientIp = WebUtils.getRemoteIp(request);
			CardCouponsOrder order = result.getRetval();
			HashMap data = new HashMap();
			data.put("paystatus", order.getPaystatus());
			data.put("tradeno", order.getTradeno());
			Map<String, String> payparams = wxMpPayService.getJsapiPayJsonParams(order , clientIp);
			data.put("payparams", payparams);
			return this.showJsonSuccess(model, data);
		}
		return this.showJsonError(model, result.getMsg());
	}
	
	@RequestMapping("/activity/metroCityPayresult.xhtml")
	public String payresult(ModelMap model){
		ElecCardBatch elecCardBatch = elecCardService.getElecCardBatch(249L, null);
		model.put("elecCardBatch", elecCardBatch);
		if(elecCardBatch.getDaynum() > 0){
			model.put("begintime", DateUtil.getMillTimestamp());
			model.put("endtime", DateUtil.addDay(DateUtil.getMillTimestamp(), elecCardBatch.getDaynum()));
		}
		return "/acitvity/getCouponResultMuch3.vm";
	}
	
}
