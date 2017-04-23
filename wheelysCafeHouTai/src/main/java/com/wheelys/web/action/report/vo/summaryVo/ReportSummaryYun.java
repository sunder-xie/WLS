package com.wheelys.web.action.report.vo.summaryVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.model.CafeShop;

public class ReportSummaryYun extends BaseObject {

	/**
	 * 运营商summaryvo数据
	 */
	private static final long serialVersionUID = 162042434588880247L;

	@Override
	public Serializable realId() {
		return null;
	}
	private Integer shopcount;//直营店营业店铺数量
	private Integer subnetpaid; // 每家店当月实付金额总计
	private Integer subquantity; //每家店当月杯数总计
	private Integer avshopprice;//当月店均收入
	private Integer avquantity;//日均杯数
	private Integer dayshopnetpaid;//店均日均收入
	private Integer avnetpaid;//日均每杯价格
	private Date date;
	private List<CafeShop> cafeShops=new ArrayList<CafeShop>();
	private Integer subclosequantity;//停业店铺总杯数
	private Integer subclosenetpaid;//停业店铺总金额
	private Integer avclosequantity;//停业店铺日均杯数
	private Integer closeshopcount;//停业店铺数量

	public Integer getCloseshopcount() {
		return closeshopcount;
	}
	public void setCloseshopcount(Integer closeshopcount) {
		this.closeshopcount = closeshopcount;
	}
	public ReportSummaryYun(Date day) {
		super();
		this.date = DateUtil.getLastTimeOfDay(day);
		this.shopcount=0;
		this.subnetpaid=0;
		this.subquantity=0;
		this.avshopprice=0;
		this.avquantity=0;
		this.avnetpaid=0;
		this.subclosequantity=0;
		this.subclosenetpaid=0;
		this.avclosequantity=0;
		this.closeshopcount=0;
		this.dayshopnetpaid=0;
	}
	public ReportSummaryYun() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Integer getDayshopnetpaid() {
		return dayshopnetpaid;
	}
	public void setDayshopnetpaid(Integer dayshopnetpaid) {
		this.dayshopnetpaid = dayshopnetpaid;
	}
	public Integer getSubclosequantity() {
		return subclosequantity;
	}
	public void setSubclosequantity(Integer subclosequantity) {
		this.subclosequantity = subclosequantity;
	}
	public Integer getSubclosenetpaid() {
		return subclosenetpaid;
	}
	public void setSubclosenetpaid(Integer subclosenetpaid) {
		this.subclosenetpaid = subclosenetpaid;
	}
	public Integer getAvclosequantity() {
		return avclosequantity;
	}
	public void setAvclosequantity(Integer avclosequantity) {
		this.avclosequantity = avclosequantity;
	}
	public List<CafeShop> getCafeShops() {
		return cafeShops;
	}
	public void setCafeShops(List<CafeShop> cafeShops) {
		this.cafeShops = cafeShops;
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
	public void setShopcount(Integer shopcount) {
		this.shopcount = shopcount;
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
	public Integer getAvshopprice() {
		return avshopprice;
	}
	public void setAvshopprice(Integer avshopprice) {
		this.avshopprice = avshopprice;
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
	
}
