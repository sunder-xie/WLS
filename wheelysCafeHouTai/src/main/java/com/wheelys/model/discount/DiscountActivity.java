package com.wheelys.model.discount;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class DiscountActivity extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1115815139039512077L;
	private Long id; // ID
	private String name; // 活动名称
	private String type; // 类型（买一送一，首杯x元,特定商品特价,打折）
	private Integer discountrate; // 折扣率
	private Integer fixamount; // 首杯价格
	private Integer limitcup; // 满多少件
	private Integer limitamount; // 满多少元
	private Integer discountamount; // 减多少元
	private Timestamp begintime; // 天数开始时间
	private Timestamp endtime; // 天数结束时间
	private Timestamp createtime; // 创建时间
	private Timestamp updatetime; // 更新时间
	private String shopids; // 店铺ID
	private String validitemid; // 品类id
	private String validproductid; // 品项id
	private String status; // Z 已中止
	private String discountmark; // 优惠标记
	private String remark; // 备注
	private String priority; // 优先级 L=1,低 2,中 3,高
	private String week; // 周期
	private String hourfrom; // 开始时间段
	private String hourto; // 结束时间段
	private String addprice; // 首杯x元 特浓加价3元
	private Integer limitmaxnum;	//总数量
	private Integer allowaddnum;	//卖出数

	public DiscountActivity() {
		super();
	}

	public DiscountActivity(String type) {
		super();
		this.type = type;
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = this.createtime;
		this.status = "Y";
		this.hourfrom = "00:00"; // 默认活动开始时间
		this.hourto = "23:59"; // 默认活动结束时间
		this.week = "1,2,3,4,5,6,7";// 周期默认全选
		this.discountrate = 0; // 折扣率默认为0
		this.fixamount = 0; // 首杯价格默认为0
		this.discountamount = 0; // 减多少元
		this.limitamount = 0; // 满多少元
		this.limitcup = 0; // 满多少件
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddprice() {
		return addprice;
	}

	public void setAddprice(String addprice) {
		this.addprice = addprice;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getFixamount() {
		return fixamount;
	}

	public void setFixamount(Integer fixamount) {
		this.fixamount = fixamount;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getShopids() {
		return shopids;
	}

	public void setShopids(String shopids) {
		this.shopids = shopids;
	}

	public Integer getDiscountrate() {
		return discountrate;
	}

	public void setDiscountrate(Integer discountrate) {
		this.discountrate = discountrate;
	}

	@Override
	public Serializable realId() {
		return id;
	}

	// 判断活动处于何种状态
	public String getActive() {
		Timestamp curDate = DateUtil.getMillTimestamp();
		boolean flag = curDate.after(endtime);
		if (StringUtils.equals(this.status, "Z") && !flag) {
			return "已中止";
		}
		if (curDate.before(begintime)) {
			return "未到期";
		}
		if (curDate.after(endtime)) {
			return "已过期";
		}
		return "优惠中";
	}

	// 判断中止开启状态
	public String getbz() {
		Timestamp curDate = DateUtil.getMillTimestamp();
		if (curDate.after(endtime)) {
			return "y";
		}
		if (StringUtils.equals(this.status, "Z")) {
			return "k";
		}
		return "z";
	}
	
	public Integer getLimitmaxnum() {
		return limitmaxnum;
	}

	public void setLimitmaxnum(Integer limitmaxnum) {
		this.limitmaxnum = limitmaxnum;
	}

	public Integer getAllowaddnum() {
		return allowaddnum;
	}

	public void setAllowaddnum(Integer allowaddnum) {
		this.allowaddnum = allowaddnum;
	}
}
