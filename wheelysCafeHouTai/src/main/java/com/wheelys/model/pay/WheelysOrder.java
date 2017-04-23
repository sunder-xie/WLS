package com.wheelys.model.pay;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class WheelysOrder extends BaseObject {

	private static final long serialVersionUID = -4751563296662992180L;

	private Long id; // ID
	private Integer version; // 更新版本
	private Long shopid; // 店铺ID
	private String ordertitle; // 订单标题
	private String tradeno; // 订单号
	private String mobile; // 联系手机
	private Timestamp createtime; // 用户下单时间
	private Timestamp paidtime; // 付款时间
	private Timestamp finishtime; // 完成时间
	private Timestamp taketime; // 取杯时间
	private Timestamp validtime; // 有效时间
	private Timestamp updatetime; // 修改时间
	private String status; // 订单状态
	private String paystatus; // 付款状态
	private String restatus; // 是否删除
	private Long memberid; // 关联用户
	private String membername; // 用户名
	private String payseqno; // 外部订单号
	private Integer totalfee; // 订单总金额
	private Integer payfee; // 需要支付的金额
	private Integer netpaid; // 支付金额
	private Integer cardpaid; // 卡支付金额
	private Long cardid; // 卡券ID
	private Long batchid; // 卡券批次ID
	private Integer quantity; // 数量
	private String citycode; // 城市代码
	private String pricategory; // 订单分类
	private String category; // 订单类别
	private Integer buycount; // 第几单
	private Integer discount; // 订单优惠金额
	private Integer discountnum;// 折扣杯数
	private String disreason; // 优惠理由
	private Long sdid; // 特价活动id
	private String takekey; // 取杯口令
	private Long keyid; // 取杯口令iD
	private Long addressid; // 外送地址ID
	private String gatewayCode; // 支付网关代码
	private String merchantCode; // 商户号标识
	private String paymethod; // 支付方式
	private String origin; // 订单来源
	private String otherinfo; // 其他信息
	private String description; // 商品描述
	private String remark; // 特别说明
	private String ukey; // 标识Partner订单唯一用户
	private String refundfaildlinfo; // 审核失败理由
	private String ordertype;

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

	public String getOrdertitle() {
		return ordertitle;
	}

	public void setOrdertitle(String ordertitle) {
		this.ordertitle = ordertitle;
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

	public Timestamp getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(Timestamp finishtime) {
		this.finishtime = finishtime;
	}

	public Timestamp getTaketime() {
		return taketime;
	}

	public void setTaketime(Timestamp taketime) {
		this.taketime = taketime;
	}

	public Timestamp getValidtime() {
		return validtime;
	}

	public void setValidtime(Timestamp validtime) {
		this.validtime = validtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
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

	public String getRestatus() {
		return restatus;
	}

	public void setRestatus(String restatus) {
		this.restatus = restatus;
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

	public Integer getPayfee() {
		return payfee;
	}

	public void setPayfee(Integer payfee) {
		this.payfee = payfee;
	}

	public Integer getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(Integer netpaid) {
		this.netpaid = netpaid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public String getTakekey() {
		return takekey;
	}

	public void setTakekey(String takekey) {
		this.takekey = takekey;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getPricategory() {
		return pricategory;
	}

	public void setPricategory(String pricategory) {
		this.pricategory = pricategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getBuycount() {
		return buycount;
	}

	public void setBuycount(Integer buycount) {
		this.buycount = buycount;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getGatewayCode() {
		return gatewayCode;
	}

	public void setGatewayCode(String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
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

	public String getRefundfaildlinfo() {
		return refundfaildlinfo;
	}

	public void setRefundfaildlinfo(String refundfaildlinfo) {
		this.refundfaildlinfo = refundfaildlinfo;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public Integer getCardpaid() {
		return cardpaid;
	}

	public void setCardpaid(Integer cardpaid) {
		this.cardpaid = cardpaid;
	}

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public Long getBatchid() {
		return batchid;
	}

	public void setBatchid(Long batchid) {
		this.batchid = batchid;
	}

	public Long getKeyid() {
		return keyid;
	}

	public void setKeyid(Long keyid) {
		this.keyid = keyid;
	}

	public Long getAddressid() {
		return addressid;
	}

	public void setAddressid(Long addressid) {
		this.addressid = addressid;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}
