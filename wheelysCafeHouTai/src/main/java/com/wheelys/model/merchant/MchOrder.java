package com.wheelys.model.merchant;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeShop;

public class MchOrder extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4795387345809733085L;
	private Long id; // ID
	private String tradeno; // 订单号
	private String ordertitle; // 订单title
	private Long mchid; // 商户ID
	private Long shopid; // 店铺ID
	private String mchname; // 商户名
	private String shopname; // 店铺名
	private String citycode; // 城市code
	private String cityname; // 城市名
	private String username; // 操作用户
	private String mobile; // 联系电话
	private String status; // 状态
	private String paystatus; // 付款状态
	private Integer totalfee; // 总金额
	private Integer discount; // 优惠金额
	private Integer payfee; // 需要支付的金额
	private Integer netpaid; // 实付金额
	private Timestamp paidtime;// 支付时间
	private Timestamp sendtime;// 发货时间
	private Timestamp createtime;// 创建时间
	private Timestamp updatetime;// 修改时间
	private Long weight; // 重量
	private String address; // 邮寄地址
	private String contacts;// 联系人
	private Integer quantity; // 数量
	private Integer expressfee; // 快递费
	private String expressDes; // 物流费用描述
	private String disreason; // 优惠理由
	private String expressInfo; // 物流信息
	private String otherinfo;//其他信息
	private String remark; // 备注
	private Long supplierid;//供销商id
	private String acceptstatus;//已接单为Y，未接单为N
	public MchOrder() {}

	public MchOrder(CafeShop cafeShop, String isexpress) {
		this.mchid = cafeShop.getShopid();
		this.shopid = cafeShop.getShopid();
		this.mchname = cafeShop.getShopname();
		this.shopname = cafeShop.getShopname();
		this.citycode = cafeShop.getCitycode();
		this.address = cafeShop.getShopaddress();
		if(StringUtils.equals(isexpress, "Y")){
			this.contacts = cafeShop.getContacts();
			this.mobile = cafeShop.getShoptelephone();
		}
		this.supplierid = cafeShop.getSupplierid();
		this.status = CafeOrderConstant.STATUS_NEW;
		this.citycode = CityContant.CITYCODE_SH;
		this.paystatus = CafeOrderConstant.STATUS_NEW;
		this.cityname = CityContant.CITYMAP.get(this.citycode);
		this.discount = 0;
		this.netpaid = 0;
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = this.createtime;
		this.acceptstatus="N";
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public Integer getExpressfee() {
		return expressfee;
	}

	public void setExpressfee(Integer expressfee) {
		this.expressfee = expressfee;
	}

	public String getDisreason() {
		return disreason;
	}

	public void setDisreason(String disreason) {
		this.disreason = disreason;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getOrdertitle() {
		return ordertitle;
	}

	public void setOrdertitle(String ordertitle) {
		this.ordertitle = ordertitle;
	}

	public Long getMchid() {
		return mchid;
	}

	public void setMchid(Long mchid) {
		this.mchid = mchid;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getMchname() {
		return mchname;
	}

	public void setMchname(String mchname) {
		this.mchname = mchname;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Integer getPayfee() {
		return payfee;
	}

	public void setPayfee(Integer payfee) {
		this.payfee = payfee;
	}

	public Integer getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(Integer netpaid) {
		this.netpaid = netpaid;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpressInfo() {
		return expressInfo;
	}

	public void setExpressInfo(String expressInfo) {
		this.expressInfo = expressInfo;
	}

	public String getExpressDes() {
		return expressDes;
	}

	public void setExpressDes(String expressDes) {
		this.expressDes = expressDes;
	}

	public Timestamp getPaidtime() {
		return paidtime;
	}

	public void setPaidtime(Timestamp paidtime) {
		this.paidtime = paidtime;
	}

	public Timestamp getSendtime() {
		return sendtime;
	}

	public void setSendtime(Timestamp sendtime) {
		this.sendtime = sendtime;
	}
	
	
	public Long getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(Long supplierid) {
		this.supplierid = supplierid;
	}
	
	public String getAcceptstatus() {
		return acceptstatus;
	}

	public void setAcceptstatus(String acceptstatus) {
		this.acceptstatus = acceptstatus;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	@Override
	public Serializable realId() {
		return id;
	}
	
	
}
