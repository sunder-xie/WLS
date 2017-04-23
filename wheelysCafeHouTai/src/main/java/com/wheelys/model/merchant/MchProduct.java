package com.wheelys.model.merchant;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

/**
 * 商家进货商品
 */
public class MchProduct extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5141077074100873525L;
	private Long id; // id
	private String ukey; // 编号
	private String name; // 商品名
	private Long itemid; // 类型ID
	private String unit; // 单位
	private String imgurl;//图片
	private Integer weight; // 重量
	private String description;// 商品描述
	private Integer price; // 价格
	private String status; // 上下架状态
	private String delstatus;// 删除状态
	private Integer sortkey;// 排序key
	private Timestamp createtime;// 创建时间
	private Timestamp updatetime;// 修改时间
	private Long supplierid;//供销商id
	private Integer sn;//排序
	public MchProduct(){}
	
	public MchProduct(String name) {
		super();
		this.name = name;
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = this.createtime;
		this.delstatus="N";
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

	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public Integer getSortkey() {
		return sortkey;
	}

	public void setSortkey(Integer sortkey) {
		this.sortkey = sortkey;
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
	
	public Long getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(Long supplierid) {
		this.supplierid = supplierid;
	}
	
	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}
