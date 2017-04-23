package com.wheelys.model.notice;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class NoticeManage extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7163646076085854199L;
	private Long id;// 编号
	private String tradeno;// 相关活动编号
	private String noticename;// 通知名称
	private String content;// 通知内容
	private String shopname;// 店铺名称
	private String shopids;// 店铺id
	private String status;// 展示状态
	private Timestamp begintime;// 开始时间
	private Timestamp endtime;// 结束时间
	private String delstatus;// 删除状态

	public NoticeManage() {

	}

	public NoticeManage(String noticename, String content, String shopids, Timestamp begintime, Timestamp endtime) {
		this.noticename = noticename;
		this.content = content;
		this.shopids = shopids;
		this.begintime = begintime;
		this.endtime = endtime;
		this.delstatus = "N";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getBegintime() {
		return begintime;
	}

	public void setBegintime(Timestamp begintime) {
		this.begintime = begintime;
	}

	public Timestamp getEndtime() {
		return endtime;
	}

	public void setEndtime(Timestamp endtime) {
		this.endtime = endtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getNoticename() {
		return noticename;
	}

	public void setNoticename(String noticename) {
		this.noticename = noticename;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getShopids() {
		return shopids;
	}

	public void setShopids(String shopids) {
		this.shopids = shopids;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}
