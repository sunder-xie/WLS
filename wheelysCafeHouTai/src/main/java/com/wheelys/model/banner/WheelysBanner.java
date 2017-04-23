package com.wheelys.model.banner;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class WheelysBanner extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1522836815519145225L;
	private Long id;
	private Integer sn; // 序号
	private Long shopid;	//店铺ID
	private String title;//标题
	private String tourl; // 指向地址
	private String imageurl; // 图片地址
	private String type; // banner类型
	private String showstatus; // 展示状态
	private String delstatus; // 删除状态
	private Timestamp begintime; // 开始展示时间
	private Timestamp endtime; // 结束展示时间
	private Timestamp createtime; // 创建时间
	private Timestamp updatetime; // 更新时间
	
	public WheelysBanner() {
		super();
	}

	public WheelysBanner(String imageurl) {
		super();
		this.imageurl = imageurl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTourl() {
		return tourl;
	}

	public void setTourl(String tourl) {
		this.tourl = tourl;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getShowstatus() {
		return showstatus;
	}

	public void setShowstatus(String showstatus) {
		this.showstatus = showstatus;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
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
