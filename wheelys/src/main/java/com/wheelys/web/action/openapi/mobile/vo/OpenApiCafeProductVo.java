package com.wheelys.web.action.openapi.mobile.vo;

import com.wheelys.util.BeanUtil;
import com.wheelys.model.CafeProduct;

public class OpenApiCafeProductVo {

	private Long id;// 主键
	private String name;// 名称
	private String enname;// 英文名称
	private Integer price;// 价格
	private String img;// 图片
	private Integer cafehot;// 冷热
	private Integer cafebean;// 咖啡豆
	private Integer cafemilk;// 牛奶

	public OpenApiCafeProductVo(CafeProduct product, String picpath) {
		BeanUtil.copyProperties(this, product);
		this.img = picpath + this.img;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
