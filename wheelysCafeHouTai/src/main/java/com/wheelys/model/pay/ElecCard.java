package com.wheelys.model.pay;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.constant.ElecCardConstant;

/**
 * 优惠券
 * @author liufeng
 *
 */
public class ElecCard extends BaseObject {
	
	private static final long serialVersionUID = 3754546507279229426L;
	private Long id;
	private String cardno; // 卡号
	private Long ebatchid; // 批次ID
	private String cardpass; // 密码
	private String status; // 状态
	private Long memberid; // 拥有者
	private String mobile; // 获取手机或绑定标志
	private Long orderid; // 使用的订单号
	private Integer version; //
	private Timestamp begintime;
	private Timestamp endtime;
	private String remark; // 废弃备注说明
	private Timestamp bindtime; // 绑定时间
	private Double useamount; // 使用金额

	public ElecCard() {
	}

	public ElecCard(String cardno) {
		this.cardno = cardno;
		this.status = ElecCardConstant.STATUS_NEW;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardpass() {
		return cardpass;
	}

	public void setCardpass(String cardpass) {
		this.cardpass = cardpass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getEbatchid() {
		return ebatchid;
	}

	public void setEbatchid(Long ebatchid) {
		this.ebatchid = ebatchid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getBindtime() {
		return bindtime;
	}

	public void setBindtime(Timestamp bindtime) {
		this.bindtime = bindtime;
	}

	public Double getUseamount() {
		return useamount;
	}

	public void setUseamount(Double useamount) {
		this.useamount = useamount;
	}

	@Override
	public Serializable realId() {
		return id;
	}
}
