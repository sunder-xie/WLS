package com.wheelys.model.orderrefund;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.model.pay.WheelysOrder;

public class RefundOrder extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8411445595358146600L;
	/**
	 * 
	 */

	private Long id;
	private Integer version;// 版本号
	private String ordertype;// 订单类型
	private Long roleid;// 店长id
	private Long refunduserid;// 退款用户id
	private String membername;// 退款用户名字
	private Long userid;// 用户id;
	private String refundtype;// 退款类型 all全额退款，part部分退款，supplement调整价格
	private String tradeno;// 订单id
	private Long shopid; // 店铺ID
	private String shopname;// 店铺名称
	private Integer shoptype;// 店铺类型
	private String shoprole;// 店长名字
	private String shoptelephone;// 手机
	private Integer refundamount;// 订单退款金额
	private Timestamp createtime;// 下单时间
	private Timestamp refundtime;// 申请退款时间
	private Timestamp refundcompletetime;// 退款完成时间
	private Timestamp updatetime;// 更新时间
	private String status;// 状态
	private String refundinfo;// 退款理由
	private String refundfaildlinfo;// 审核失败理由
	private String paystatus;// 支付状态
	private String paymethod;// 支付方法
	private String category;// 订单类别
	private Integer netpaid;// 支付金额
	private Long operaterid;// 操作用户id
	private String operatename;// 操作用户名字

	public RefundOrder() {
		super();
	}

	public RefundOrder(String tradeno, WheelysOrder order, Long userid, String refundinfo, Integer refundamount) {
		this.version = 0;
		this.tradeno = tradeno;
		this.status = "pending_audit";
		this.ordertype = "0";
		this.paystatus = "paid";
		this.refundtype = "all";
		this.refundtime = DateUtil.getMillTimestamp();
		this.shopid = order.getShopid();
		this.refunduserid = order.getMemberid();
		this.membername = order.getMembername();
		this.roleid = order.getMemberid();
		this.createtime = order.getCreatetime();
		this.refundinfo = refundinfo;
		this.refundamount = refundamount;
		this.netpaid = order.getNetpaid();
		this.userid = userid;
		this.category = order.getCategory();

	}

	public Integer getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(Integer netpaid) {
		this.netpaid = netpaid;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getRefundinfo() {
		return refundinfo;
	}

	public void setRefundinfo(String refundinfo) {
		this.refundinfo = refundinfo;
	}

	public String getRefundfaildlinfo() {
		return refundfaildlinfo;
	}

	public void setRefundfaildlinfo(String refundfaildlinfo) {
		this.refundfaildlinfo = refundfaildlinfo;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getRoleid() {
		return roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public Long getRefunduserid() {
		return refunduserid;
	}

	public void setRefunduserid(Long refunduserid) {
		this.refunduserid = refunduserid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getRefundtype() {
		return refundtype;
	}

	public void setRefundtype(String refundtype) {
		this.refundtype = refundtype;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public Integer getShoptype() {
		return shoptype;
	}

	public void setShoptype(Integer shoptype) {
		this.shoptype = shoptype;
	}

	public String getShoprole() {
		return shoprole;
	}

	public void setShoprole(String shoprole) {
		this.shoprole = shoprole;
	}

	public String getShoptelephone() {
		return shoptelephone;
	}

	public void setShoptelephone(String shoptelephone) {
		this.shoptelephone = shoptelephone;
	}

	public Integer getRefundamount() {
		return refundamount;
	}

	public void setRefundamount(Integer refundamount) {
		this.refundamount = refundamount;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getRefundtime() {
		return refundtime;
	}

	public void setRefundtime(Timestamp refundtime) {
		this.refundtime = refundtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getOperaterid() {
		return operaterid;
	}

	public void setOperaterid(Long operaterid) {
		this.operaterid = operaterid;
	}

	public String getOperatename() {
		return operatename;
	}

	public void setOperatename(String operatename) {
		this.operatename = operatename;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getRefundcompletetime() {
		return refundcompletetime;
	}

	public void setRefundcompletetime(Timestamp refundcompletetime) {
		this.refundcompletetime = refundcompletetime;
	}

}
