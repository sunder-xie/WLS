package com.wheelys.web.action.openapi.vo;

import com.wheelys.model.CafeProduct;

public class OpneApiCafeProductVo {

	private Long id;// 主键
	private Long itemid;// 分类
	private String name;// 名称9不能用优惠券
	private String enname;// 英文名称
	private Integer price;// 价格
	private String img;// 图片
	private Integer cafehot;// 冷热
	private Integer cafebean;// 咖啡豆
	private Integer cafemilk;// 牛奶

	public OpneApiCafeProductVo(CafeProduct product,String imgpath) {
		super();
		this.id = product.getId();
		this.itemid = product.getId();
		this.name = product.getName();
		this.enname = product.getEnname();
		this.price = product.getPrice();
		this.img = imgpath+product.getImg();
		this.cafehot = product.getCafehot();
		this.cafebean = product.getCafebean();
		this.cafemilk = product.getCafemilk();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getCafehot() {
		return cafehot;
	}

	public void setCafehot(Integer cafehot) {
		this.cafehot = cafehot;
	}

	public Integer getCafebean() {
		return cafebean;
	}

	public void setCafebean(Integer cafebean) {
		this.cafebean = cafebean;
	}

	public Integer getCafemilk() {
		return cafemilk;
	}

	public void setCafemilk(Integer cafemilk) {
		this.cafemilk = cafemilk;
	}

}
