package com.wheelys.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class CafeProduct extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2582652731864712854L;
	private Long id;// 主键
	private Long itemid;// 分类
	private String name;// 名称
	private String enname;// 英文名称
	private String desp;// 简介
	private Integer price;// 价格
	private String img;// 图片
	private Integer cafehot;// 冷热
	private Integer cafebean;// 咖啡豆
	private Integer cafemilk;// 牛奶
	private Integer addtime;// 添加商品时间
	private Integer display;// 1显示，-1删除
	private Timestamp createtime;
	private Timestamp updatetime;

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

	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
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

	public Integer getAddtime() {
		return addtime;
	}

	public void setAddtime(Integer addtime) {
		this.addtime = addtime;
	}

	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
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

	@Override
	public Serializable realId() {
		return id;
	}

}
