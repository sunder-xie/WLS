package com.wheelys.web.action.report.vo.summaryVo;

import java.io.Serializable;
import java.util.Date;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class ReportSummaryZi extends BaseObject {
	/**
	 * 自营店summary数据统计
	 */
	private static final long serialVersionUID = 7678521339523374473L;
	private Integer shopcount;//直营店营业店铺数量
	private Long shopid; // 店铺id
	private Integer totalday;//每家店当月经营天数
	private Integer subnetpaid; // 每家店当月实付金额总计
	private Integer subquantity; //每家店当月杯数总计
	private Integer avquantity;//日均店均杯数
	private Integer dayshopnetpaid;//日均店均价格
	private Integer avnetpaid;//日均每杯价格
	private Date date;
	private Integer diffDay;//一月的天数
	
	
	public ReportSummaryZi(Date day) {
		super();
		this.date = DateUtil.getLastTimeOfDay(day);
		this.shopcount=0;
		this.totalday=0;
		this.subnetpaid=0;
		this.subquantity=0;
		this.avquantity=0;
		this.avnetpaid=0;
		this.dayshopnetpaid=0;
	}
	public ReportSummaryZi() {
		super();
	}
	
	public Integer getDayshopnetpaid() {
		return dayshopnetpaid;
	}
	public void setDayshopnetpaid(Integer dayshopnetpaid) {
		this.dayshopnetpaid = dayshopnetpaid;
	}
	public Integer getDiffDay() {
		return diffDay;
	}
	public void setDiffDay(Integer diffDay) {
		this.diffDay = diffDay;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getShopcount() {
		return shopcount;
	}
	public Integer getAvquantity() {
		return avquantity;
	}

	public void setAvquantity(Integer avquantity) {
		this.avquantity = avquantity;
	}

	public Integer getAvnetpaid() {
		return avnetpaid;
	}

	public void setAvnetpaid(Integer avnetpaid) {
		this.avnetpaid = avnetpaid;
	}

	public void setShopcount(Integer shopcount) {
		this.shopcount = shopcount;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public Integer getTotalday() {
		return totalday;
	}

	public void setTotalday(Integer totalday) {
		this.totalday = totalday;
	}

	public Integer getSubnetpaid() {
		return subnetpaid;
	}

	public void setSubnetpaid(Integer subnetpaid) {
		this.subnetpaid = subnetpaid;
	}

	public Integer getSubquantity() {
		return subquantity;
	}

	public void setSubquantity(Integer subquantity) {
		this.subquantity = subquantity;
	}

	@Override
	public Serializable realId() {
		return null;
	}
	

}
