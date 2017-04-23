package com.wheelys.model.report;

import java.io.Serializable;
import java.util.Date;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class ReportOrderMemberShopDate extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 288554060008046462L;
	
	private Long id;
	private Long shopid;
	private String shopname;
	private Date day;
	private String dailykey;
	private Integer newregisteredmemberorder; //新注册首单用户数
	private Integer firstordernum; //老用户首单单数
	private Integer firstorderusernum; //首单老用户数
	private Integer sumdailyordernum; // 总订单数
	private Integer sumdailyorderusernum; // 总用户数
	private Integer sumdailyorderpaidfee;//总金额
	
	public ReportOrderMemberShopDate(){}
	
	public ReportOrderMemberShopDate(Long shopid, String shopname, Date day) {
		super();
		this.shopid = shopid;
		this.shopname = shopname;
		this.day = day;
		this.dailykey = shopid.toString() + "_" + DateUtil.formatDate(day);
		this.newregisteredmemberorder = 0;
		this.firstorderusernum = 0;
		this.firstordernum = 0;
	}

	public Integer getRepurchasenum(){
		return sumdailyorderusernum - firstorderusernum - newregisteredmemberorder;
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

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Integer getSumdailyorderpaidfee() {
		return sumdailyorderpaidfee;
	}

	public void setSumdailyorderpaidfee(Integer sumdailyorderpaidfee) {
		this.sumdailyorderpaidfee = sumdailyorderpaidfee;
	}

	public String getDailykey() {
		return dailykey;
	}

	public void setDailykey(String dailykey) {
		this.dailykey = dailykey;
	}

	public Integer getFirstordernum() {
		return firstordernum;
	}

	public void setFirstordernum(Integer firstordernum) {
		this.firstordernum = firstordernum;
	}

	public Integer getFirstorderusernum() {
		return firstorderusernum;
	}

	public void setFirstorderusernum(Integer firstorderusernum) {
		this.firstorderusernum = firstorderusernum;
	}

	public Integer getSumdailyordernum() {
		return sumdailyordernum;
	}

	public void setSumdailyordernum(Integer sumdailyordernum) {
		this.sumdailyordernum = sumdailyordernum;
	}

	public Integer getSumdailyorderusernum() {
		return sumdailyorderusernum;
	}

	public void setSumdailyorderusernum(Integer sumdailyorderusernum) {
		this.sumdailyorderusernum = sumdailyorderusernum;
	}

	public Integer getNewregisteredmemberorder() {
		return newregisteredmemberorder;
	}

	public void setNewregisteredmemberorder(Integer newregisteredmemberorder) {
		this.newregisteredmemberorder = newregisteredmemberorder;
	}

	@Override
	public Serializable realId() {
		return id;
	}
	
}
