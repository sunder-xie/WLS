package com.wheelys.web.action.admin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wheelys.Config;
import com.wheelys.model.acl.User;
import com.wheelys.model.acl.WebModule;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.service.admin.CommonService;
import com.wheelys.untrans.OSSUploadFileService;
import com.wheelys.web.action.AnnotationController;

@Controller
public class CommonController extends AnnotationController {

	@Autowired
	@Qualifier("config")
	private Config config;
	@Autowired
	@Qualifier("commonService")
	private CommonService commonService;
	@Autowired
	@Qualifier("ossUploadFileService")
	private OSSUploadFileService ossUploadFileService;

	/**
	 * 
	 * @param model菜单显示
	 * @return
	 */
	@RequestMapping("/admin/common/menu.xhtml")
	public String showWebModule(ModelMap model){
		User user = getLogonMember();
		Map<WebModule, List<WebModule>> webModuleMap =commonService.showWebModuleMap(user.getId());
		model.put("webModuleMap", webModuleMap);
		return "/admin/common/usermenu.vm";
	}

	@RequestMapping("/admin/shop/uploadimg.xhtml")
	public String uoloadimg(HttpServletRequest request, ModelMap model) {
		Map<String, String> imgMap = ossUploadFileService.uploadImg(request, MchProduct.class);
		model.put("imgMap", imgMap);
		return "/admin/upload/uploadimg.vm";
	}
	
	@RequestMapping("/admin/shop/touploadimg.xhtml")
	public String touploadimg(String imgurl, ModelMap model) {
		Map<String, String> imgMap = new HashMap<String, String>();
		if(StringUtils.isNotBlank(imgurl)){
			imgMap.put("imgurl", imgurl);
			model.put("imgMap", imgMap);
		}
		return "/admin/upload/uploadimg.vm";
	}

	@RequestMapping("/ueditor/getConfig.xhtml")
	@ResponseBody
	public String ueditor(ModelMap model, HttpServletRequest request, String action){
		if(StringUtils.equals(action, "uploadimage")){
			Map<String, String> imgMap = ossUploadFileService.uploadImg(request, WheelysNews.class);
			model.put("imgMap", imgMap);
			String url = imgMap.get("upfile");
			if(StringUtils.isNotBlank(url)){
				String name = StringUtils.substring(url, StringUtils.lastIndexOf(url, "/")+1);
				String type = StringUtils.substring(name, StringUtils.lastIndexOf(name, ".")+1);
				imgMap.put("original", name);
				imgMap.put("type", type);
				imgMap.put("name", name);
				imgMap.put("url", url);
				imgMap.put("state", "SUCCESS");
			}
			return JsonUtils.writeMapToJson(imgMap);
		}
		Map picConfig = new HashMap();
		picConfig.put("imageActionName", "uploadimage");
		picConfig.put("imageFieldName", "upfile");
		picConfig.put("imageUrlPrefix", config.getString("picPath"));
		return JsonUtils.writeMapToJson(picConfig);
	}
	
}