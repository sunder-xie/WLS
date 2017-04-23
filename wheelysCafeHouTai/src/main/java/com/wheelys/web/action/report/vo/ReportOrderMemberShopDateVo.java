package com.wheelys.web.action.report.vo;

import com.wheelys.util.DateUtil;
import com.wheelys.model.report.ReportOrderMemberShopDate;

public class ReportOrderMemberShopDateVo {

	private Long shopid;
	private String shopname;
	private String day;
	private Integer newregisteredmemberorder; // 新注册首单用户数
	private Integer firstordernum; // 老用户首单单数
	private Integer firstorderusernum; // 首单老用户数
	private Integer sumdailyordernum; // 总订单数
	private Integer sumdailyorderusernum; // 总用户数
	private Integer sumdailyorderpaidfee;// 总金额
	private Integer repurchasenum;

	public ReportOrderMemberShopDateVo() {
		super();
	}

	public ReportOrderMemberShopDateVo(ReportOrderMemberShopDate reportVo) {
		this.shopid = reportVo.getShopid();
		this.shopname = reportVo.getShopname();
		this.day = DateUtil.formatDate(reportVo.getDay());
		this.newregisteredmemberorder = reportVo.getNewregisteredmemberorder();
		this.firstordernum = reportVo.getFirstordernum();
		this.firstorderusernum = reportVo.getFirstorderusernum();
		this.repurchasenum = reportVo.getRepurchasenum();
		this.sumdailyordernum = reportVo.getSumdailyordernum();
		this.sumdailyorderpaidfee = reportVo.getSumdailyorderpaidfee();
		this.sumdailyorderusernum = reportVo.getSumdailyorderusernum();
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

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getNewregisteredmemberorder() {
		return newregisteredmemberorder;
	}

	public void setNewregisteredmemberorder(Integer newregisteredmemberorder) {
		this.newregisteredmemberorder = newregisteredmemberorder;
	}

	public Integer getFirstordernum() {
		return firstordernum;
	}

	public void setFirstordernum(Integer firstordernum) {
		this.firstordernum = firstordernum;
	}

	public Integer getRepurchasenum() {
		return repurchasenum;
	}

	public void setRepurchasenum(Integer repurchasenum) {
		this.repurchasenum = repurchasenum;
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

	public Integer getSumdailyorderpaidfee() {
		return sumdailyorderpaidfee;
	}

	public void setSumdailyorderpaidfee(Integer sumdailyorderpaidfee) {
		this.sumdailyorderpaidfee = sumdailyorderpaidfee;
	}

}
