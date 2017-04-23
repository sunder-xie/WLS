package com.wheelys.web.action.wap.vo;

import java.sql.Timestamp;

import com.wheelys.model.pay.ElecCard;
import com.wheelys.model.pay.ElecCardBatch;

public class ElecCardVo {

	private Long id;
	private String cardname; // 卡的名称
	private String dhname; // 兑换名称
	private String cardtype; // 卡类型 ,1 抵值优惠券 兑换券 , 默认为抵值优惠券
	private Integer amount; // 券额
	private Integer maxprice; // 兑换券最高兑换额,默认为0
	private Integer minprice; // 抵值优惠群最低消费,默认为0
	private Long ebatchid; // 批次ID
	private String shopid;//店铺ID
	private String productid;//商品ID
	private String annexation; 
	private String status; // 状态
	private Long orderid; // 使用的订单号
	private Timestamp begintime;
	private Timestamp endtime;
	private String remark; // 废弃备注说明
	private String description;//备注
	private Timestamp bindtime; // 绑定时间
	private Integer useamount; // 使用金额
	private String amountmark;//1,特价金额  2,抵扣金额
	private String desc;
	private Integer sortkey;
	
	public ElecCardVo(){}
	
	public ElecCardVo(ElecCard card, ElecCardBatch batch) {
		this.id = card.getId();
		this.dhname = batch.getDhname();
		this.cardtype = batch.getCardtype();
		this.shopid = batch.getValidshopid();
		this.productid = batch.getValidproductid();
		this.amount = batch.getAmount();
		this.maxprice = batch.getMaxprice();
		this.minprice =  batch.getMinprice();
		this.cardname = batch.getCardname();
		this.amountmark = batch.getAmountmark();
		this.ebatchid = batch.getId();
		this.status = card.getStatus();
		this.begintime = card.getBegintime();
		this.endtime = card.getEndtime();
		this.remark = card.getRemark();
		this.bindtime = card.getBindtime();
		this.description = batch.getDescription();
		this.annexation = batch.getAnnexation();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAnnexation() {
		return annexation;
	}

	public void setAnnexation(String annexation) {
		this.annexation = annexation;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getDhname() {
		return dhname;
	}

	public void setDhname(String dhname) {
		this.dhname = dhname;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public Long getEbatchid() {
		return ebatchid;
	}

	public void setEbatchid(Long ebatchid) {
		this.ebatchid = ebatchid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getBindtime() {
		return bindtime;
	}

	public void setBindtime(Timestamp bindtime) {
		this.bindtime = bindtime;
	}

	public Integer getUseamount() {
		return useamount;
	}

	public void setUseamount(Integer useamount) {
		this.useamount = useamount;
	}

	public Integer getSortkey() {
		return sortkey;
	}

	public void setSortkey(Integer sortkey) {
		this.sortkey = sortkey;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAmountmark() {
		return amountmark;
	}

	public void setAmountmark(String amountmark) {
		this.amountmark = amountmark;
	}

}
