package com.wheelys.web.action.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.web.action.AnnotationController;

@Controller
public class HuangController extends AnnotationController {
	//注册
	@RequestMapping("/test/register.xhtml")
	public String register(){
		return "/wps/register.vm";
	}
	//登录
	@RequestMapping("/test/login.xhtml")
	public String login(){
		return "/wps/login.vm";
	}

	//商店详情
	@RequestMapping("/test/shopDetail.xhtml")
	public String storeDetails(){
		return "/wps/shopDetail.vm";
	}
	//订单生成页
	@RequestMapping("/test/forOrder.xhtml")
	public String forOrder(){
		return "/wps/confirmOrderComplete.vm";
	}
	//我的订单
	@RequestMapping("/test/myOrder.xhtml")
	public String myOrder(){
		return "/wps/myOrder.vm";
	}
	//我的订单新
	@RequestMapping("/test/myOrderNew.xhtml")
	public String myOrderNew(){
		return "/wps/myOrder_new.vm";
	}
	//订单详情
	@RequestMapping("/test/orderDetail.xhtml")
	public String orderDetail(){
		return "/wps/orderDetail.vm";
	}
	//购买完成页
	@RequestMapping("/test/payEnd.xhtml")
	public String payEnd(){
		return "/wps/payresult.vm";
	}
	//搜索页面
	@RequestMapping("/test/search.xhtml")
	public String search(){
		return "/wps/search.vm";
	}
	//错误页面
	@RequestMapping("/test/erro.xhtml")
	public String erro(){
		return "/wps/erro.vm";
	}
	//测试
	@RequestMapping("/test/demo.xhtml")
	public String demo(){
		return "/demo/scale.vm";
	}
	//新闻列表
	@RequestMapping("/test/newsList.xhtml")
	public String newsList(){
		return "/wps/news/newsList.vm";
	}
	//新闻详情
	@RequestMapping("/test/newsDetail.xhtml")
	public String newsDetail(){
		return "/wps/news/newsDetail.vm";
	}
	//获取订单
	@RequestMapping("/test/getDemo.xhtml")
	public String getDemo(){
		return "/wps/getDemo.vm";
	}
	//个人中心
	@RequestMapping("/test/myCenter.xhtml")
	public String myCenter(){
		return "/wps/myCenter.vm";
	}
	//获取优惠券拉窗
	@RequestMapping("/test/couponPage.xhtml")
	public String couponPage(){
		return "/wps/modul/couponPage.vm";
	}
	//优惠券中心
	@RequestMapping("/test/couponCenter.xhtml")
	public String couponCenter(){
		return "/wps/couponCenter.vm";
	}
}
