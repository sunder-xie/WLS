package com.wheelys.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;

public class CartProduct extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7524087844773251247L;
	private Long id; // id
	private String ukey; // 用户唯一标示
	private String pkey; // 商品key
	private Long productid; // 商品ID
	private Integer quantity;// 数量
	private String otherinfo;// hot,milk
	private Integer price; // 价格
	private Timestamp createtime;
	private Integer totalfee;
	private Map<String, String> map;
	private CafeProduct product; 
	
	public CartProduct(){}

	public CartProduct(String ukey, Long productid, Integer quantity, String otherinfo) {
		super();
		this.ukey = ukey;
		this.productid = productid;
		if(quantity != null){
			this.quantity = quantity;
			if(this.quantity < 1){
				this.quantity = 1;
			}
			if(this.quantity > 99){
				this.quantity = 99;
			}
		}
		this.otherinfo = otherinfo;
		this.createtime = DateUtil.getMillTimestamp();
		if(StringUtils.isNotBlank(otherinfo)){
			this.map = JsonUtils.readJsonToMap(otherinfo);
		}else{
			this.map = new HashMap();
		}
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

	public String getPkey() {
		return getKey();
	}	
	
	private String getKey() {
		if(StringUtils.isNotBlank(pkey)){
			return this.pkey.replace("key_", "");
		}
		return StringUtils.isBlank(otherinfo) ? productid+"" : productid+"_"+otherinfo.replace("\"", "");
	}

	public void setPkey(String pkey) {
		this.pkey = pkey;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		if(this.quantity < 1){
			this.quantity = 1;
		}
		if(this.quantity > 99){
			this.quantity = 99;
		}
	}

	public Integer getTotalfee() {
		return totalfee;
	}

	public void setTotalfee(Integer totalfee) {
		this.totalfee = totalfee;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public CafeProduct getProduct() {
		return product;
	}

	public void setProduct(CafeProduct product) {
		this.product = product;
	}

	public Map<String, String> getMap() {
		if(StringUtils.isNotBlank(otherinfo)){
			this.map = JsonUtils.readJsonToMap(otherinfo);
		}else{
			this.map = new HashMap();
		}
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public Serializable realId() {
		return id;
	}

}
