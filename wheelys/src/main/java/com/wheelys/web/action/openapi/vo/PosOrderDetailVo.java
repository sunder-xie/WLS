package com.wheelys.web.action.openapi.vo;

public class PosOrderDetailVo {

	private long productid;	//咖啡ID
	private String otherinfo;//{hot:hot/cold,milk:y,bean:y}
	private int quantity;	//数量
	
	public long getProductid() {
		return productid;
	}

	public void setProductid(long productid) {
		this.productid = productid;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getOtherinfo() {
		return otherinfo;
	}

	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}

}
