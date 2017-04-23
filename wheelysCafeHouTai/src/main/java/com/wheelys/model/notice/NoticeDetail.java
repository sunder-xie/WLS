package com.wheelys.model.notice;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class NoticeDetail extends BaseObject {
	/**
		 * 
		 */
	private static final long serialVersionUID = -8212173175354991686L;
	private Long id;
	private Long noticeid;// 通知编号
	private Long shopid;// 店铺id
	private Timestamp begintime;// 开始时间
	private Timestamp endtime;// 结束时间
	private Timestamp updatetime;// 更新时间
	private String delstatus;// 删除状态

	public NoticeDetail() {

	}

	public NoticeDetail(Long noticeid, Long shopid, Timestamp begintime, Timestamp endtime) {
		this.noticeid = noticeid;
		this.shopid = shopid;
		this.begintime = begintime;
		this.endtime = endtime;
		this.updatetime = DateUtil.addDay(DateUtil.getMillTimestamp(), -1);// 昨天
		this.delstatus = "N";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNoticeid() {
		return noticeid;
	}

	public void setNoticeid(Long noticeid) {
		this.noticeid = noticeid;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
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

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
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
