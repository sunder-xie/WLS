package com.wheelys.web.action.openapi.vo;

import com.wheelys.model.merchant.MchProduct;
import com.wheelys.util.VmUtils;

public class OpneApiMchProductVo {

	private Long id; // id
	private String ukey; // 编号
	private String name; // 商品名
	private Long itemid; // 类型ID
	private String imgurl;	//图片
	private String unit; // 单位
	private Integer weight; // 重量
	private String description;// 商品描述
	private Double price; // 价格

	
	public OpneApiMchProductVo(MchProduct mchProduct, String picpath) {
		this.id = mchProduct.getId();
		this.ukey = mchProduct.getUkey();
		this.name = mchProduct.getName();
		this.itemid = mchProduct.getItemid();
		this.unit = mchProduct.getUnit();
		this.weight = mchProduct.getWeight();
		this.description = mchProduct.getDescription();
		this.price = VmUtils.getDoubleAmount(mchProduct.getPrice());
		this.imgurl = picpath + mchProduct.getImgurl();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
