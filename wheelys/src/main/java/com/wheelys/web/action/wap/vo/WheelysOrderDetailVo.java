package com.wheelys.web.action.wap.vo;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.util.BeanUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.order.WheelysOrderDetail;

public class WheelysOrderDetailVo {

	private Long id; // 主键
	private Long orderid; // 订单ID
	private Long productid; // 商品ID
	private Integer price; // 单价
	private String productname;// 商品名称
	private String productename;// 商品名称
	private String productimg; // 图片
	private String description;// {冷1热2默认0}
	private Integer quantity; // 数量
	private Integer totalfee; // 总价
	private Integer discount; // 折扣
	private Integer paidfee; // 需要支付的金额
	private Integer discountnum;// 折扣杯数
	
	public WheelysOrderDetailVo(WheelysOrderDetail detail){
		BeanUtil.copyProperties(this, detail);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getProductename() {
		return productename;
	}

	public void setProductename(String productename) {
		this.productename = productename;
	}

	public String getProductimg() {
		return productimg;
	}

	public void setProductimg(String productimg) {
		this.productimg = productimg;
	}

	public String getDescription() {
		Map<String, String> map = JsonUtils.readJsonToMap(description);
		String des = "";
		if(StringUtils.equals(map.get("hot"), "hot") || StringUtils.equals(map.get("hot"), "y")){
			des = "热";
		}
		if(StringUtils.equals(map.get("bean"), "y")){
			if(StringUtils.isNotBlank(des)) des += ".";
			des += "特浓";
		}
		if(StringUtils.equals(map.get("milk"), "y")){
			if(StringUtils.isNotBlank(des)) des += ".";
			des += "加奶";
		}
		if(StringUtils.isNotBlank(des)) des = "( " +des+ " )";
		return des;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Integer getPaidfee() {
		return paidfee;
	}

	public void setPaidfee(Integer paidfee) {
		this.paidfee = paidfee;
	}

	public Integer getDiscountnum() {
		return discountnum;
	}

	public void setDiscountnum(Integer discountnum) {
		this.discountnum = discountnum;
	}

}
