package com.wheelys.web.action.openapi.vo;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.util.VmUtils;

public class OpenApiOrderVo {

	private Long id; // ID
	private String tradeno; // 订单号
	private String mobile; // 联系手机
	private Timestamp paidtime; // 付款时间
	private Timestamp taketime; //取杯时间
	private Timestamp maketime;	//制作时间
	private Timestamp updatetime; // 更新时间
	private String status; // 订单状态
	private String paystatus; // 付款状态
	private String category;//类型
	private Long memberid; // 关联用户
	private String membername; // 用户名
	private String totalfee; // 订单总金额
	private String payfee; // 需要支付的金额
	private String netpaid; // 支付金额
	private String discount; // 订单优惠
	private Integer quantity; // 数量
	private String takekey; // 取杯口令
	private Long shopid; // 关联场馆
	private Integer discountnum;// 折扣杯数
	private String disreason; // 优惠理由
	private String refundfaildlinfo;//审核失败理由

	public OpenApiOrderVo() {
		super();
	}

	public OpenApiOrderVo(WheelysOrder order) {
		super();
		this.id = order.getId();
		this.tradeno = order.getTradeno();
		this.mobile = order.getMobile();
		this.paidtime = order.getPaidtime();
		this.updatetime = order.getUpdatetime();
		if(!StringUtils.equals(order.getCategory(), CafeOrderConstant.CATEGORY_RESERVED)){
			this.taketime = this.paidtime;
			this.maketime = this.paidtime;
		}else{
			Map map = JsonUtils.readJsonToMap(order.getOtherinfo());
			Integer reservedtime = (Integer) map.get("reservedtime");
			if(reservedtime == null){
				reservedtime = 0;
			}
			this.taketime = DateUtil.addMinute(order.getPaidtime(), reservedtime);
			this.maketime = DateUtil.addMinute(this.taketime, -3);
		}
		this.status = order.getStatus();
		this.paystatus = order.getPaystatus();
		this.memberid = order.getMemberid();
		this.membername = order.getMembername();
		this.category = order.getCategory();
		this.totalfee = VmUtils.getAmount(order.getTotalfee());
		this.payfee = VmUtils.getAmount(order.getPayfee());
		this.discount = VmUtils.getAmount(order.getDiscount());
		this.netpaid = VmUtils.getAmount(order.getNetpaid());
		this.quantity = order.getQuantity();
		this.takekey = order.getTakekey();
		this.shopid = order.getShopid();
		this.discountnum = order.getDiscountnum();
		this.disreason = order.getDisreason();
		this.refundfaildlinfo = order.getRefundfaildlinfo();
	
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Timestamp getMaketime() {
		return maketime;
	}

	public void setMaketime(Timestamp maketime) {
		this.maketime = maketime;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getTakekey() {
		return takekey;
	}

	public void setTakekey(String takekey) {
		this.takekey = takekey;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(String totalfee) {
		this.totalfee = totalfee;
	}

	public String getPayfee() {
		return payfee;
	}

	public void setPayfee(String payfee) {
		this.payfee = payfee;
	}

	public String getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(String netpaid) {
		this.netpaid = netpaid;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getRefundfaildlinfo() {
		return refundfaildlinfo;
	}

	public void setRefundfaildlinfo(String refundfaildlinfo) {
		this.refundfaildlinfo = refundfaildlinfo;
	}
	
}
