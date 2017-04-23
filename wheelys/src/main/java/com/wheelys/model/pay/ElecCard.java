package com.wheelys.model.pay;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.model.user.WheelysMember;

/**
 * 优惠券
 * @author liufeng
 *
 */
public class ElecCard extends BaseObject {
	
	private static final long serialVersionUID = 3754546507279229426L;
	private Long id;
	private Long ebatchid; // 批次ID
	private Long memberid; // 拥有者
	private Long orderid; // 使用的订单ID
	private String cardtype;//类型
	private String status; // 状态
	private String mobile; // 获取手机或绑定标志
	private Integer version; //
	private Timestamp begintime;
	private Timestamp endtime;
	private String remark; // 废弃备注说明
	private Timestamp bindtime; // 绑定时间
	private Integer costprice;	//成本价
	private Integer paidamount; // 使用金额
	private Timestamp updatetime; // 绑定时间
	private String ukey;	//唯一标识,批次ID+用户ID+序号

	public ElecCard() {}

	public ElecCard(WheelysMember member, ElecCardBatch elecCardBatch, int seqno, String key) {
		this.paidamount = 0;
		this.cardtype = elecCardBatch.getCardtype();
		this.status = ElecCardConstant.STATUS_SOLD;
		this.ebatchid = elecCardBatch.getId();
		this.memberid = member.getId();
		this.mobile = member.getMobile();
		this.begintime = elecCardBatch.getTimefrom();
		this.endtime = elecCardBatch.getTimeto();
		this.updatetime = DateUtil.getMillTimestamp();
		this.bindtime = DateUtil.getMillTimestamp();
		this.ukey = elecCardBatch.getId() + "_" + member.getId() + "_" + seqno + "_" + key;
		if(elecCardBatch.getDaynum() != null && elecCardBatch.getDaynum() > 0){
			this.begintime = this.bindtime;
			this.endtime = DateUtil.addDay(this.bindtime, elecCardBatch.getDaynum());
		}
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

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
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
	
	public Integer getPaidamount() {
		return paidamount;
	}

	public void setPaidamount(Integer paidamount) {
		this.paidamount = paidamount;
	}

	public Integer getCostprice() {
		return costprice;
	}

	public void setCostprice(Integer costprice) {
		this.costprice = costprice;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
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
