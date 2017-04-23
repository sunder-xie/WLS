package com.wheelys.web.action.wap.vo;

import java.sql.Timestamp;

import com.wheelys.util.BeanUtil;
import com.wheelys.model.discount.DiscountActivity;

public class DiscountActivityVo {

	private Long id; // ID
	private String name; // 活动名称
	private String type; // 类型
	private Integer discountrate; // 折扣率
	private Integer fixamount; // 首杯价格
	private Integer limitcup; // 满多少件
	private Integer limitamount; // 满多少元
	private Integer discountamount; // 减多少元
	private Timestamp begintime; // 开始时间
	private Timestamp endtime; // 结束时间
	private String hourfrom; // 开始时间段
	private String hourto; // 结束时间段
	private String week; // 周期
	private String shopids; // 店铺ID
	private String validitemid; // 品类id
	private String validproductid; // 品项id
	private String discountmark; // 优惠标记
	private String priority; // 优先级 L=1,低 2,中 3,高
	private String addprice; // 首杯x元 特浓加价3元
	private Timestamp createtime; // 创建时间
	private Timestamp updatetime; // 更新时间
	private Integer sortkey;

	public DiscountActivityVo(DiscountActivity discountActivity) {
		super();
		BeanUtil.copyProperties(this, discountActivity);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(Integer discountrate) {
		this.discountrate = discountrate;
	}

	public Integer getFixamount() {
		return fixamount;
	}

	public void setFixamount(Integer fixamount) {
		this.fixamount = fixamount;
	}

	public Integer getLimitcup() {
		return limitcup;
	}

	public void setLimitcup(Integer limitcup) {
		this.limitcup = limitcup;
	}

	public Integer getLimitamount() {
		return limitamount;
	}

	public void setLimitamount(Integer limitamount) {
		this.limitamount = limitamount;
	}

	public Integer getDiscountamount() {
		return discountamount;
	}

	public void setDiscountamount(Integer discountamount) {
		this.discountamount = discountamount;
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

	public String getHourfrom() {
		return hourfrom;
	}

	public void setHourfrom(String hourfrom) {
		this.hourfrom = hourfrom;
	}

	public String getHourto() {
		return hourto;
	}

	public void setHourto(String hourto) {
		this.hourto = hourto;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getShopids() {
		return shopids;
	}

	public void setShopids(String shopids) {
		this.shopids = shopids;
	}

	public String getValiditemid() {
		return validitemid;
	}

	public void setValiditemid(String validitemid) {
		this.validitemid = validitemid;
	}

	public String getValidproductid() {
		return validproductid;
	}

	public void setValidproductid(String validproductid) {
		this.validproductid = validproductid;
	}

	public String getDiscountmark() {
		return discountmark;
	}

	public void setDiscountmark(String discountmark) {
		this.discountmark = discountmark;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getAddprice() {
		return addprice;
	}

	public void setAddprice(String addprice) {
		this.addprice = addprice;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Integer getSortkey() {
		return sortkey;
	}

	public void setSortkey(Integer sortkey) {
		this.sortkey = sortkey;
	}

}
