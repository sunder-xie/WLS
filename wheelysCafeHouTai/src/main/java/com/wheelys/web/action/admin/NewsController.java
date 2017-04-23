package com.wheelys.web.action.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.model.acl.User;
import com.wheelys.util.JsonUtils;
import com.wheelys.web.util.PageUtil;
import com.wheelys.model.news.WheelysNews;
import com.wheelys.model.news.WheelysNewsContent;
import com.wheelys.service.admin.NewsContentService;
import com.wheelys.service.admin.NewsService;
import com.wheelys.util.WebUtils;
import com.wheelys.web.action.AnnotationController;

@Controller
public class NewsController extends AnnotationController {

	@Autowired
	@Qualifier("newsService")
	private NewsService newsService;
	@Autowired
	@Qualifier("newsContentService")
	private NewsContentService newsContentService;

	@RequestMapping("/admin/content/newsList.xhtml")
	public String newsList(ModelMap model, Integer pageNo, String title, Timestamp fromdate, Timestamp todate, String category) {		
		if (pageNo == null)
			pageNo = 0;
		int rowsPerPage = 10;
		List<WheelysNews> newsList = newsService.searchNewsList(title, fromdate, todate, category);
		int count = newsService.getNewsListCount(title, fromdate, todate, category);
		PageUtil pageUtil = new PageUtil(count, rowsPerPage, pageNo, "admin/content/newsList.xhtml");
		model.put("newsList", newsList);
		model.put("pageUtil", pageUtil);
		return "/admin/news/newsList.vm";
	}

	@RequestMapping("/admin/content/updatePublishstatus.xhtml")
	public String updateStatus(Long id,HttpServletRequest request){
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:"+JsonUtils.writeMapToJson(params));
		newsService.updatePublishstatus(id);
		return "redirect:/admin/content/newsList.xhtml";
	}

	@RequestMapping("/admin/content/deleteNews.xhtml")
	public String deleteNews(Long id) {
		newsService.deleteNews(id);
		return "redirect:/admin/content/newsList.xhtml";
	}

	@RequestMapping("/admin/content/showNewsDetail.xhtml")
	public String showNews(ModelMap model, Long id) {
		if (id == null) {
			return "/admin/news/editNewsDetail.vm";
		}
		WheelysNews news = newsService.getNewsById(id);
		WheelysNewsContent newsContent = newsContentService.getNewsContentByNewsid(id);
		model.put("news", news);
		model.put("newsContent", newsContent);
		return "/admin/news/editNewsDetail.vm";
	}

	@RequestMapping("/admin/content/editNewsDetail.xhtml")
	public String editNews(Long id, String newsimg, String title,HttpServletRequest request, String writer, String overview, String content,
			String category, Timestamp publishtime) {
		Map<String, String> params = WebUtils.getRequestMap(request);
		User user = getLogonMember();
		params.put("username", user.getUsername());
		dbLogger.warn("params:"+JsonUtils.writeMapToJson(params));
		if (id == null) {
			newsService.saveNews(title, newsimg, writer, overview, content, category, publishtime);
		} else {
			newsService.updateNews(id, newsimg, title, writer, overview, content, category, publishtime);
		}
		return "redirect:/admin/content/newsList.xhtml";
	}
	
}
