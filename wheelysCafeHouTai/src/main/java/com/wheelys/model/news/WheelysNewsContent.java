package com.wheelys.model.news;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class WheelysNewsContent extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6814988915378166963L;
	
	private Long newsid;
	private String newscontent; // 文章内容 
	
	public WheelysNewsContent(){}
	
	public WheelysNewsContent(WheelysNews wheelysNews){
		this.newsid = wheelysNews.getId();
	}

	public Long getNewsid() {
		return newsid;
	}

	public void setNewsid(Long newsid) {
		this.newsid = newsid;
	}

	public String getNewscontent() {
		return newscontent;
	}

	public void setNewscontent(String newscontent) {
		this.newscontent = newscontent;
	}

	@Override
	public Serializable realId() {
		return newsid;
	}
}
