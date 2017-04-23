package com.wheelys.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.util.StringUtil;

/**
 * 店铺配送地址
 * @author liufeng
 *
 */
public class ShopExpressAddress extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1258422989366491983L;
	private Long id;				//ID
	private Long shopid;			//店铺ID
	private String address;			//配送地址
	private String ukey;			//ukey
	private Timestamp createtime;	//创建时间
	private Timestamp updatetime;	//更新时间
	
	public ShopExpressAddress(){}
	
	public ShopExpressAddress(CafeShop shop, String address){
		this.shopid = shop.getShopid();
		this.address = address;
		this.ukey = StringUtil.md5(shopid+"_"+address);
		this.createtime = DateUtil.getMillTimestamp();
		this.updatetime = this.createtime;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}
