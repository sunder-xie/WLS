package com.wheelys.web.action.openapi.vo;

import java.sql.Timestamp;
import java.util.List;

import com.wheelys.util.BeanUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.pay.WheelysOrder;

public class WheelysOerderVo {

	private Long id; // ID
	private Long shopid; // 店铺ID
	private String ordertitle; // 订单标题
	private String tradeno; // 订单号
	private String mobile; // 联系手机
	private Timestamp createtime; // 用户下单时间
	private Timestamp paidtime; // 付款时间
	private Timestamp taketime; // 取杯时间
	private String status; // 订单状态
	private String paystatus; // 付款状态
	private Long memberid; // 关联用户
	private String membername; // 用户名
	private String payseqno; // 外部订单号
	private Integer totalfee; // 订单总金额
	private String refundamount; // 退款金额
	private Integer payfee; // 需要支付的金额
	private String netpaid; // 支付金额
	private Integer quantity; // 数量
	private Integer discount; // 订单优惠
	private Integer discountnum;// 折扣杯数
	private String disreason; // 优惠理由
	private String category;// 类型
	private Long sdid; // 特价活动id
	private String takekey; // 取杯口令
	private String refundstatus;// 退款状态
	private String refundfaildlinfo;// 审核失败理由
	private String paymethod; // 支付方法
	private Long keyid;
	private CafeShop shop;
	private CafeShopProfile cafeShopProfile;
	private List<WheelysOrderDetailVo> detailList;
	private String productName; // 产品名称
	private String activityType; // 优惠方式

	public WheelysOerderVo() {
	}

	public WheelysOerderVo(WheelysOrder order) {
		BeanUtil.copyProperties(this, order);
	}

	public CafeShopProfile getCafeShopProfile() {
		return cafeShopProfile;
	}

	public void setCafeShopProfile(CafeShopProfile cafeShopProfile) {
		this.cafeShopProfile = cafeShopProfile;
	}

	public String getRefundstatus() {
		return refundstatus;
	}

	public void setRefundstatus(String refundstatus) {
		this.refundstatus = refundstatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getOrdertitle() {
		return ordertitle;
	}

	public void setOrdertitle(String ordertitle) {
		this.ordertitle = ordertitle;
	}

	public String getRefundamount() {
		return refundamount;
	}

	public void setRefundamount(String refundamount) {
		this.refundamount = refundamount;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getKeyid() {
		return keyid;
	}

	public void setKeyid(Long keyid) {
		this.keyid = keyid;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getPaidtime() {
		return paidtime;
	}

	public void setPaidtime(Timestamp paidtime) {
		this.paidtime = paidtime;
	}

	public Timestamp getTaketime() {
		return taketime;
	}

	public void setTaketime(Timestamp taketime) {
		this.taketime = taketime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getPayseqno() {
		return payseqno;
	}

	public void setPayseqno(String payseqno) {
		this.payseqno = payseqno;
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getPayfee() {
		return payfee;
	}

	public void setPayfee(Integer payfee) {
		this.payfee = payfee;
	}

	public String getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(String netpaid) {
		this.netpaid = netpaid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getDiscount() {
		return discount;
	}

	public Integer getDiscountnum() {
		return discountnum;
	}

	public void setDiscountnum(Integer discountnum) {
		this.discountnum = discountnum;
	}

	public String getDisreason() {
		return disreason;
	}

	public void setDisreason(String disreason) {
		this.disreason = disreason;
	}

	public Long getSdid() {
		return sdid;
	}

	public void setSdid(Long sdid) {
		this.sdid = sdid;
	}

	public String getTakekey() {
		return takekey;
	}

	public void setTakekey(String takekey) {
		this.takekey = takekey;
	}

	public CafeShop getShop() {
		return shop;
	}

	public void setShop(CafeShop shop) {
		this.shop = shop;
	}

	public String getRefundfaildlinfo() {
		return refundfaildlinfo;
	}

	public void setRefundfaildlinfo(String refundfaildlinfo) {
		this.refundfaildlinfo = refundfaildlinfo;
	}

	public List<WheelysOrderDetailVo> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<WheelysOrderDetailVo> detailList) {
		this.detailList = detailList;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

}
