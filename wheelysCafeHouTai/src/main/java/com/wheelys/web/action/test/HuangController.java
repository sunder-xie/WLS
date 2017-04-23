package com.wheelys.web.action.test;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.service.DaoService;
import com.wheelys.support.ErrorCode;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.pay.WeiXinPayUtil;
import com.wheelys.service.cafe.CafeKeyService;
import com.wheelys.service.cafe.OrderService;
import com.wheelys.service.report.OrderReportService;
import com.wheelys.untrans.OSSUploadFileService;
import com.wheelys.util.SendMessageUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class HuangController extends AnnotationController {

	@Autowired@Qualifier("ossUploadFileService")
	private OSSUploadFileService ossUploadFileService;
	@Autowired@Qualifier("cafeKeyService")
	private CafeKeyService cafeKeyService;
	@Autowired@Qualifier("orderReportService")
	private OrderReportService orderReportService;
	@Autowired@Qualifier("orderService")
	private OrderService orderService;
	@Autowired@Qualifier("daoService")
	private DaoService daoService;

	@RequestMapping("/test/refund.xhtml")
	@ResponseBody
	public String refund(String tradeno){
		WheelysOrder order = daoService.getObjectByUkey(WheelysOrder.class, "tradeno", tradeno);
		if(order.getShopid().intValue() == 6 || order.getMemberid().intValue() == 2 || order.getMemberid().intValue() == 1344){
			ErrorCode<Map<String, String>> result = WeiXinPayUtil.refund(order, order.getNetpaid(), "R"+order.getTradeno());
			String ret = JsonUtils.writeMapToJson(result.getRetval());
			dbLogger.warn("退款成功："+ret);
			return "退款成功："+ret;
		}
		return "此订单不能操作！！！";
	}
	
	public static void main(String[] args) {
		WheelysOrder order = new WheelysOrder();
		order.setNetpaid(100);
		order.setTradeno("A170109212803824");
		//order.setPayseqno("4007362001201701095778632889");
		ErrorCode<Map<String, String>> result = WeiXinPayUtil.refund(order, order.getNetpaid(), "R"+order.getTradeno());
		String ret = JsonUtils.writeMapToJson(result.getRetval());
		System.out.println(ret);
	}
	
	@RequestMapping("/test/index.xhtml")
	@ResponseBody
	public String index(){
		for (int i = 0; i < 30; i++) {
			orderReportService.toDayReportProduct(null, DateUtil.addDay( new Date(), -i));
			dbLogger.warn("退款成功："+DateUtil.addDay( new Date(), -i));
		}
		return "/index.vm";
	}
	
	@RequestMapping("/test/uploadImg.xhtml")
	public String uploadImg(ModelMap model){
		SendMessageUtils.sendMessage("18516561021", "您的补货订单P**555已经成功提交，我们会及时处理");
		System.out.println("您的补货订单P**555已经成功提交，我们会及时处理".length());
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/generatedata.xhtml")
	public void generateData(){
		orderReportService.generateData(DateUtil.addDay(new Date(), -1));
	}
	
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf('.');
		return (sepIndex != -1 ? path.substring(sepIndex + 1) : null);
	}
	@RequestMapping("/test/indexaaa.xhtml")
	public String generamethodteData() {
		for (int i = 0; i < 120; i++){
			orderReportService.generamethodteData(DateUtil.addDay(new Date(), -i));
		}
		return "/hello.vm";
	}
	
}
