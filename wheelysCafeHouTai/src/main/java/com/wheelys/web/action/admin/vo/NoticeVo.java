package com.wheelys.web.action.admin.vo;

import com.wheelys.model.notice.NoticeManage;

public class NoticeVo {

	private String noticename;// 通知名称
	private String content;// 通知内容
	private NoticeManage noticeManage;
	private String shopname;

	public NoticeVo() {}

	public NoticeVo(String noticename, String content) {
		this.noticename = noticename;
		this.content = content;
	
	}
	
	
	public NoticeManage getNoticeManage() {
		return noticeManage;
	}

	public void setNoticeManage(NoticeManage noticeManage) {
		this.noticeManage = noticeManage;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getNoticename() {
		return noticename;
	}

	public void setNoticename(String noticename) {
		this.noticename = noticename;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
