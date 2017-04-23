package com.wheelys.model.news;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.util.VmUtils;

public class WheelysNews extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4852326861385557956L;
	
	private Long id;
	private String title; // 标题
	private String overview; // 简介
	private String writer; // 作者
	private String newspicture; // 文章图片路径
	private String category; // 文章分类
	private Integer pageview; // 阅读量
	private String publishstatus; // 发布状态 Y/N
	private String delstatus; // 删除状态
	private Timestamp publishtime; // 发布时间
	private Timestamp appeartime; // 发表时间(指定的)
	private Timestamp createtime; 
	private Timestamp updatetime;
	
	public WheelysNews(){}
	
	public WheelysNews(String title, String writer, String overview, String category, Timestamp appeartime){
		this.title = title;
		this.writer = writer;
		this.overview = overview;
		this.category = category;
		this.pageview = 1;
		this.publishstatus = "N";
		this.delstatus = "N";
		this.createtime = DateUtil.getMillTimestamp();
		if(VmUtils.isEmpObj(appeartime)){appeartime = this.createtime;}
		this.appeartime = appeartime;
		this.updatetime = this.createtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getNewspicture() {
		return newspicture;
	}

	public void setNewspicture(String newspicture) {
		this.newspicture = newspicture;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPageview() {
		return pageview;
	}

	public void setPageview(Integer pageview) {
		this.pageview = pageview;
	}

	public String getPublishstatus() {
		return publishstatus;
	}

	public void setPublishstatus(String publishstatus) {
		this.publishstatus = publishstatus;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public Timestamp getPublishtime() {
		return publishtime;
	}

	public void setPublishtime(Timestamp publishtime) {
		this.publishtime = publishtime;
	}

	public Timestamp getAppeartime() {
		return appeartime;
	}

	public void setAppeartime(Timestamp appeartime) {
		this.appeartime = appeartime;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	@Override
	public Serializable realId() {
		return id;
	}
}
