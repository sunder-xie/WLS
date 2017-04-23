package com.wheelys.web.action.report.vo;

import java.io.Serializable;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.model.report.ReportOrderDatePaymethod;

public class ReportOrderPayMethodVo extends BaseObject {

	private static final long serialVersionUID = -2929797208366548298L;
	private String date; // 查询时间
	private Long shopid; // 店铺id
	private Integer totalfee; // 原价金额/理论金额
	private Integer totalquantity;//合计杯数
	private String avprice;	 //理论平均单价
	private Integer totalprice;//实际金额总计
	private Integer wnetpaid; // 微信实付金额
	private Integer wquantity; // 微信杯数
	private Integer znetpaid; // 支付宝实付金额
	private Integer zquantity; //支付宝 杯数
	private String sellmethod;//销售方式
	
	//销售方式
	public ReportOrderPayMethodVo(ReportOrderDatePaymethod reportpaymethod) {
		super();
		this.date= DateUtil.formatDate(reportpaymethod.getDate());
		this.shopid=reportpaymethod.getShopid();
		this.totalquantity=0;
		this.totalfee=0;
		this.totalprice=0;
		this.wnetpaid=0;
		this.wquantity=0;
		this.znetpaid=0;
		this.zquantity=0;
		this.avprice="";
	}

	public ReportOrderPayMethodVo() {
		super();
	}
	
	public String getSellmethod() {
		return sellmethod;
	}

	public void setSellmethod(String sellmethod) {
		this.sellmethod = sellmethod;
	}

	public Integer getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Integer totalprice) {
		this.totalprice = totalprice;
	}

	public Integer getTotalquantity() {
		return totalquantity;
	}

	public void setTotalquantity(Integer totalquantity) {
		this.totalquantity = totalquantity;
	}

	public Integer getWnetpaid() {
		return wnetpaid;
	}

	public void setWnetpaid(Integer wnetpaid) {
		this.wnetpaid = wnetpaid;
	}

	public Integer getWquantity() {
		return wquantity;
	}

	public void setWquantity(Integer wquantity) {
		this.wquantity = wquantity;
	}

	public Integer getZnetpaid() {
		return znetpaid;
	}

	public void setZnetpaid(Integer znetpaid) {
		this.znetpaid = znetpaid;
	}

	public Integer getZquantity() {
		return zquantity;
	}

	public void setZquantity(Integer zquantity) {
		this.zquantity = zquantity;
	}

	public Serializable realId() {
		return null;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public String getAvprice() {
		return avprice;
	}

	public void setAvprice(String avprice) {
		this.avprice = avprice;
	}


}
