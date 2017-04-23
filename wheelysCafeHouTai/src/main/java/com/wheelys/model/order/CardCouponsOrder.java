package com.wheelys.model.order;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.cons.Status;
import com.wheelys.constant.CafeOrderConstant;
import com.wheelys.constant.PayConstant;
import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class CardCouponsOrder extends BaseObject{

	private static final long serialVersionUID = 6414782854694340139L;
	private Long id;					//ID
	private Integer version;			//更新版本
	private String ordertitle;		//订单标题
	private String tradeno;			//订单号
	private String mobile;			//联系手机
	private Timestamp createtime;	//用户下单时间
	private Timestamp paidtime;		//付款时间
	private Timestamp validtime;		//有效时间
	private Timestamp updatetime;	//修改时间
	private Long shopid;			//店铺ID
	private String ebatchid;		//批次ID
	private String status;			//订单状态
	private String paystatus;		//付款状态
	private String restatus;			//是否删除
	private Long memberid;			//关联用户
	private String membername;		//用户名
	private String payseqno;			//外部订单号
	private Integer payfee;			//需要支付的金额
	private Integer netpaid;			//支付金额
	private Integer quantity;		//数量
	private String citycode;			//城市代码
	private String category;			//订单类别
	private String gatewayCode;		//支付网关代码	
	private String merchantCode;		//商户号标识
	private String paymethod;		//支付方法
	private String origin;			//订单来源
	private String otherinfo;		//其他信息
	private String description;		//商品描述
	private String remark;			//特别说明
	private String ukey;				//标识Partner订单唯一用户

	public CardCouponsOrder(){}

	public CardCouponsOrder(Long memberid, String membername, String ukey, String citycode){
		this.version = 0;
		this.createtime = new Timestamp(System.currentTimeMillis());
		this.updatetime = this.createtime;
		this.paymethod = PayConstant.PAYMETHOD_UNKNOWN;
		this.category = CafeOrderConstant.CATEGORY_TAKE;
		this.status = CafeOrderConstant.STATUS_NEW;
		this.paystatus = CafeOrderConstant.STATUS_NEW;
		this.validtime = DateUtil.addDay(this.createtime, 1);
		this.restatus = Status.N;
		this.netpaid = 0;
		this.memberid = memberid;
		this.membername = membername;
		this.citycode = citycode;
		this.ukey = ukey;
		this.otherinfo = "{}";
	}

	@Override
	public Serializable realId() {
		return id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getOrdertitle() {
		return ordertitle;
	}

	public void setOrdertitle(String ordertitle) {
		this.ordertitle = ordertitle;
	}

	public String getTradeno() {
		return tradeno;
	}

	public void setTradeno(String tradeno) {
		this.tradeno = tradeno;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getPaidtime() {
		return paidtime;
	}

	public void setPaidtime(Timestamp paidtime) {
		this.paidtime = paidtime;
	}

	public Timestamp getValidtime() {
		return validtime;
	}

	public void setValidtime(Timestamp validtime) {
		this.validtime = validtime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public String getRestatus() {
		return restatus;
	}

	public void setRestatus(String restatus) {
		this.restatus = restatus;
	}

	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getPayseqno() {
		return payseqno;
	}

	public void setPayseqno(String payseqno) {
		this.payseqno = payseqno;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGatewayCode() {
		return gatewayCode;
	}

	public void setGatewayCode(String gatewayCode) {
		this.gatewayCode = gatewayCode;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
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

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getEbatchid() {
		return ebatchid;
	}

	public void setEbatchid(String ebatchid) {
		this.ebatchid = ebatchid;
	}

}
