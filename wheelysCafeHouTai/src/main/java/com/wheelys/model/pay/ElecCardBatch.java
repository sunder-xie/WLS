package com.wheelys.model.pay;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.util.StringUtil;

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
	private Integer maxPrice; // 兑换券最高兑换额,默认为0
	private String dhname; // 兑换名称
	private Integer minPrice; // 抵值优惠群最低消费,默认为0
	private Integer cardcount; // 优惠券数量
	private Integer costprice;//成本价
	private Integer amount; //特价金额,,抵扣金额
	private String amountmark;//1,特价金额  2,抵扣金额
	private Integer limitcup;//最少几杯能用
	private String validshopid; // 可用店铺
	private String validitemid; // 可用品类,
	private String validproductid; // 可用品项
	private String cardtype; // 卡类型 ,1 抵值优惠券 兑换券 , 默认为抵值优惠券
	private Integer daynum; // 有效期限（发送奖品之日起的有效天数）
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
	private Integer lqmoney; // 领取金额
	private Integer usecount; // 使用数
	private Integer usemoney; // 使用金额
	private Integer gqcount; // 过期数
	private String jstype;// 结算类型 1,公司单独承担 ,2,公司和运营商共同承担,3运营商单独承担
	private String delstatus;// Y 删除 N 未删除
	private Long userId;// 用户id
	private String command;// 口令
	private Integer cycleday;	//领取周期
	private String ukey;// 口令
	private String annexation;	//附赠商品id
	private Integer receivenum;//领取数

	public ElecCardBatch() {
		super();
	}

	public ElecCardBatch(String cardtype) {
		this.maxPrice =0;
		this.minPrice =0;
		this.cardtype=cardtype;
		this.category = "cafe";
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = DateUtil.getMillTimestamp();
		this.delstatus="N";
		this.cycleday = 0;
		this.ukey = StringUtil.md5(StringUtil.getUID()+System.currentTimeMillis());
		this.daynum=0;
		this.cardcount=0;
		this.amount=0;
		this.limitcup=0;
		this.jstype="1";
		this.validshopid="";
		this.validitemid="";
		this.validproductid="{}";
		this.lqcount=0;
		this.usecount=0;
		this.gqcount=0;
		this.lqmoney=0;
		this.usemoney=0;
		this.receivenum=1;
		this.cycleday=0;
		this.costprice=0;
	}

	public String getAmountmark() {
		return amountmark;
	}

	public void setAmountmark(String amountmark) {
		this.amountmark = amountmark;
	}

	public Integer getCostprice() {
		return costprice;
	}

	public void setCostprice(Integer costprice) {
		this.costprice = costprice;
	}

	public Integer getReceivenum() {
		return receivenum;
	}

	public void setReceivenum(Integer receivenum) {
		this.receivenum = receivenum;
	}

	public Integer getLimitcup() {
		return limitcup;
	}

	public void setLimitcup(Integer limitcup) {
		this.limitcup = limitcup;
	}
	
	public String getAnnexation() {
		return annexation;
	}

	public void setAnnexation(String annexation) {
		this.annexation = annexation;
	}

	public Integer getLqmoney() {
		return lqmoney;
	}

	public void setLqmoney(Integer lqmoney) {
		this.lqmoney = lqmoney;
	}

	public Integer getUsemoney() {
		return usemoney;
	}

	public void setUsemoney(Integer usemoney) {
		this.usemoney = usemoney;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public String getJstype() {
		return jstype;
	}

	public void setJstype(String jstype) {
		this.jstype = jstype;
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

	public Integer getUsecount() {
		return usecount;
	}

	public void setUsecount(Integer usecount) {
		this.usecount = usecount;
	}

	public Integer getGqcount() {
		return gqcount;
	}

	public void setGqcount(Integer gqcount) {
		this.gqcount = gqcount;
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

	public Integer getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Integer maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Integer getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Integer minPrice) {
		this.minPrice = minPrice;
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

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public Integer getCycleday() {
		return cycleday;
	}

	public void setCycleday(Integer cycleday) {
		this.cycleday = cycleday;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}