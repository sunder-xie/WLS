package com.wheelys.web.action.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.web.action.AnnotationController;

@Controller
public class LogisticsController extends AnnotationController {

	/**
	 * 
	 * 物流规则页面展示
	 */

	@RequestMapping("/admin/mch/showLogistics.xhtml")
	public String showLogistics(){
		return "/admin/mchOrder/mchLogistics.vm";
	}
	
	
}