package com.wheelys.web.action.openapi.vo;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.util.JsonUtils;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.util.VmUtils;

public class MchOrderVo {

	private Long id; // ID
	private String tradeno; // 订单号
	private String ordertitle; // 订单title
	private Long mchid; // 商户ID
	private Long shopid; // 店铺ID
	private String mchname; // 商户名
	private String shopname; // 店铺名
	private String username; // 操作用户
	private String mobile; // 联系电话
	private String status; // 状态
	private Double totalfee; // 总金额
	private Double discount; // 优惠金额
	private Double payfee; // 需要支付的金额
	private Double netpaid; // 实付金额
	private Long weight; // 重量
	private Timestamp createtime;// 创建时间
	private Timestamp updatetime;// 修改时间
	private Timestamp sendtime;	//发货时间
	private String address;	//邮寄地址
	private String contacts;//联系人
	private String remark; // 备注 
	private Double expressfee;	//物流费
	private String expressInfo;	//物流信息
	private String disreason;		//优惠理由
	private String expressDes;	//物流信息
	private String confimtime;	//订单自动确认时间
	private Integer quantity; // 数量
	private String isexpress;//是否快递
	private List<MchOrderDetailVo> detailList;
	
	public MchOrderVo(){}

	public MchOrderVo(MchOrder order) {
		this.id = order.getId();
		this.tradeno = order.getTradeno();
		this.ordertitle = order.getOrdertitle();
		this.mchid = order.getMchid();
		this.shopid = order.getShopid();
		this.mchname = order.getMchname();
		this.shopname = order.getShopname();
		this.username = order.getUsername();
		this.mobile = order.getMobile();
		this.status = order.getStatus();
		this.totalfee = VmUtils.getDoubleAmount(order.getTotalfee());
		this.discount = VmUtils.getDoubleAmount(order.getDiscount());
		this.payfee = VmUtils.getDoubleAmount(order.getPayfee());
		this.netpaid =VmUtils.getDoubleAmount(order.getNetpaid());
		this.expressfee = VmUtils.getDoubleAmount(order.getExpressfee());
		this.disreason = order.getDisreason();
		this.weight = order.getWeight();
		this.address = order.getAddress();
		this.contacts = order.getContacts();
		this.sendtime = order.getSendtime();
		this.expressInfo = order.getExpressInfo();
		this.expressDes = order.getExpressDes();
		this.remark = order.getRemark();
		this.quantity = order.getQuantity();
		this.createtime = order.getCreatetime();
		this.updatetime = order.getUpdatetime();
		if(StringUtils.isNotBlank(order.getOtherinfo())){
			Map map = JsonUtils.readJsonToMap(order.getOtherinfo());
			if(map.get("isexpress") != null){
				this.isexpress = map.get("isexpress")+"";
			}
		}
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

	public Timestamp getSendtime() {
		return sendtime;
	}

	public void setSendtime(Timestamp sendtime) {
		this.sendtime = sendtime;
	}

	public String getOrdertitle() {
		return ordertitle;
	}

	public void setOrdertitle(String ordertitle) {
		this.ordertitle = ordertitle;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getExpressDes() {
		return expressDes;
	}

	public void setExpressDes(String expressDes) {
		this.expressDes = expressDes;
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

	public Double getExpressfee() {
		return expressfee;
	}

	public void setExpressfee(Double expressfee) {
		this.expressfee = expressfee;
	}

	public String getConfimtime() {
		return confimtime;
	}

	public void setConfimtime(String confimtime) {
		this.confimtime = confimtime;
	}

	public String getExpressInfo() {
		return expressInfo;
	}

	public void setExpressInfo(String expressInfo) {
		this.expressInfo = expressInfo;
	}

	public String getDisreason() {
		return disreason;
	}

	public void setDisreason(String disreason) {
		this.disreason = disreason;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public Long getMchid() {
		return mchid;
	}

	public void setMchid(Long mchid) {
		this.mchid = mchid;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Double totalfee) {
		this.totalfee = totalfee;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getPayfee() {
		return payfee;
	}

	public void setPayfee(Double payfee) {
		this.payfee = payfee;
	}

	public Double getNetpaid() {
		return netpaid;
	}

	public void setNetpaid(Double netpaid) {
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

	public String getIsexpress() {
		return isexpress;
	}

	public void setIsexpress(String isexpress) {
		this.isexpress = isexpress;
	}

	public List<MchOrderDetailVo> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<MchOrderDetailVo> detailList) {
		this.detailList = detailList;
	}

}
