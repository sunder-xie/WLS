package com.wheelys.web.action.wap;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.Config;
import com.wheelys.service.DaoService;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.model.user.OpenMember;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.MemberService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.service.order.DiscountService;
import com.wheelys.service.order.OrderService;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.util.OAuthUtils;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.wap.vo.ElecCardVo;

@Controller
public class MemberController extends AnnotationController {

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("daoService")
	private DaoService daoService;
	@Autowired@Qualifier("discountService")
	private DiscountService discountService;
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("memberService")
	private MemberService memberService;
	@Autowired@Qualifier("elecCardService")
	private ElecCardService elecCardService;
	
	@RequestMapping("/register.xhtml")
	public String register(){
		return "/wps/register.vm";
	}

	@RequestMapping("/home/index.xhtml")
	public String index(ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"member/center.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		OpenMember openmember = memberService.getOpenMemberByMemberid(member.getId());
		model.put("member", member);
		model.put("headpic", config.getString("picPath")+member.getHeadpic());
		if(openmember != null){
			model.put("member", openmember);
			model.put("headpic", openmember.getHeadpic());
		}
		return "/wps/home/myCenter.vm";
	}

	@RequestMapping("/home/setting.xhtml")
	public String setting(ModelMap model, HttpServletRequest request){
		WheelysMember member = this.getLogonMember();
		if(member == null){
			String lastpage = config.getAbsPath()+"member/center.xhtml";
			String oauthUrl = OAuthUtils.oauth2Url(request, lastpage, config.getAbsPath());
			return showRedirect(oauthUrl, model);
		}
		model.put("member", member);
		return "/wps/home/setting.vm";
	}

	@RequestMapping("/home/help.xhtml")
	public String help(){
		return "/wps/home/help.vm";
	}

	@RequestMapping("/home/cardList.xhtml")
	public String cardList(ModelMap model){
		WheelysMember member = this.getLogonMember();
		List<ElecCardVo> cardList = elecCardService.getElecCardVoList(member.getId(), null, ElecCardConstant.STATUS_SOLD, 0, 150);
		model.put("cardList", cardList);
		return "/wps/home/cardList.vm";
	}

	@RequestMapping("/home/cardListHistory.xhtml")
	public String cardListHistory(ModelMap model){
		WheelysMember member = this.getLogonMember();
		elecCardService.unlockElecCard(member.getId());
		List<ElecCardVo> cardList = elecCardService.getElecCardVoList(member.getId(), null, ElecCardConstant.STATUS_USED, 0, 150);
		model.put("cardList", cardList);
		return "/wps/home/cardListHistory.vm";
	}

	@RequestMapping("/userAgreement.xhtml")
	public String userAgreement(){
		return "/wps/home/userAgreement.vm";
	}
	
	@RequestMapping("/about.xhtml")
	public String aboutUs(){
		return "/wps/home/aboutUs.vm";
	}
	
	@RequestMapping("/discount.xhtml")
	@ResponseBody
	public String discount(){
		List<DiscountActivity> data = daoService.getAllObjects(DiscountActivity.class);
		String str = "";
		for (DiscountActivity da : data) {
			str += "id:"+da.getId()+",name"+da.getName()+",status:"+da.getStatus()
			+",begintime:"+DateUtil.formatTimestamp(da.getBegintime())
			+",endtime:"+DateUtil.formatTimestamp(da.getEndtime())
			+",createtime"+DateUtil.formatTimestamp(da.getCreatetime())
			+",shopids"+da.getShopids()
			+",Fixamount"+da.getFixamount()+"</br>";
		}
		return str;
	}

	@RequestMapping("/setDiscount.xhtml")
	public String discount(Long discountid, String status , String shopids, Timestamp begintime, Timestamp endtime, ModelMap model){
		discountService.updateDiscount(discountid,status,shopids,begintime,endtime);
		return this.showJsonSuccess(model, null);
	}

}
