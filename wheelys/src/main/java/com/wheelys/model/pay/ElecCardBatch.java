package com.wheelys.model.pay;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

/**
 * 优惠券批次
 * 
 * @author liufeng
 *
 */
public class ElecCardBatch extends BaseObject {

	private static final long serialVersionUID = 375454650779946L;
	private Long id; // ID
	private String category; // 标识：cafe
	private Integer maxprice; // 兑换券最高兑换额,默认为0
	private Integer minprice; // 抵值优惠群最低消费,默认为0
	private String dhname; // 兑换名称
	private Integer cardcount; // 优惠券数量
	private Integer costprice;//成本价
	private Integer amount; // 券额
	private String validshopid; // 可用店铺
	private String validitemid; // 可用品类,
	private String validproductid; // 可用品项
	private String cardtype; // 卡类型 ,1 抵值优惠券 兑换券 , 默认为抵值优惠券
	private Integer daynum; // 有效期限（发送奖品之日起的有效天数）
	private Integer cycleday;	//领取周期
	private String citycode; // 店铺区域限制
	private Integer activation; // 状态 1,正常使用 ,已过期 3,已领完
	private String description; // 说明
	private String remark; // 备注
	private Timestamp timefrom; // 卡有效开始时间
	private Timestamp timeto; // 卡有效结束时间
	private Timestamp createtime; // 创建时间
	private Timestamp updatetime; // 更新时间
	private String cardname; // 卡的名称
	private Integer lqcount; // 领取数
	private String delstatus;// Y 删除 N 未删除
	private String command;// 口令
	private String ukey;// 唯一标识
	private Integer receivenum;//领取数
	private String annexation;	//附赠商品id
	private String amountmark;//1,特价金额  2,抵扣金额

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public String getDhname() {
		return dhname;
	}

	public void setDhname(String dhname) {
		this.dhname = dhname;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public Integer getLqcount() {
		return lqcount;
	}

	public void setLqcount(Integer lqcount) {
		this.lqcount = lqcount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getMaxprice() {
		return maxprice;
	}

	public void setMaxprice(Integer maxprice) {
		this.maxprice = maxprice;
	}

	public Integer getMinprice() {
		return minprice;
	}

	public void setMinprice(Integer minprice) {
		this.minprice = minprice;
	}

	public Integer getCycleday() {
		return cycleday;
	}

	public void setCycleday(Integer cycleday) {
		this.cycleday = cycleday;
	}

	public Integer getCardcount() {
		return cardcount;
	}

	public void setCardcount(Integer cardcount) {
		this.cardcount = cardcount;
	}

	public String getValidshopid() {
		return validshopid;
	}

	public void setValidshopid(String validshopid) {
		this.validshopid = validshopid;
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

	public Timestamp getTimefrom() {
		return timefrom;
	}

	public void setTimefrom(Timestamp timefrom) {
		this.timefrom = timefrom;
	}

	public Timestamp getTimeto() {
		return timeto;
	}

	public void setTimeto(Timestamp timeto) {
		this.timeto = timeto;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public Integer getDaynum() {
		return daynum;
	}

	public void setDaynum(Integer daynum) {
		this.daynum = daynum;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public Integer getCostprice() {
		return costprice;
	}

	public void setCostprice(Integer costprice) {
		this.costprice = costprice;
	}

	public Integer getActivation() {
		return activation;
	}

	public void setActivation(Integer activation) {
		this.activation = activation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getAnnexation() {
		return annexation;
	}

	public void setAnnexation(String annexation) {
		this.annexation = annexation;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public Integer getReceivenum() {
		return receivenum;
	}

	public void setReceivenum(Integer receivenum) {
		this.receivenum = receivenum;
	}

	public String getAmountmark() {
		return amountmark;
	}

	public void setAmountmark(String amountmark) {
		this.amountmark = amountmark;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}