package com.wheelys.model.merchant;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

/**
 * 
 * @author dell 物流规则
 */
public class Mchrules extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1349768278634409937L;
	private Long id;// id
	private Long supplierid;// 供销商id;
	private String citycode;// 城市code
	private Integer lessfreight;// 小于200公斤时运费
	private Integer lesscost;// 小于200公斤时配送费
	private Integer morefreight;// 大于等于200公斤时运费
	private Integer morecost;// 大于等于200公斤时配送费
	private String delstatus;// 删除

	public Mchrules() {

	}

	public Mchrules(String citycode, Long supplierid, Float lessfreight, Float lesscost, Float morefreight,
			Float morecost) {
		this.citycode = citycode;
		this.supplierid = supplierid;
		this.lessfreight = (int) (lessfreight * 100);
		this.lesscost = (int) (lesscost * 100);
		this.morefreight = (int) (morefreight * 100);
		this.morecost = (int) (morecost * 100);
		this.delstatus = "N";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public Integer getLessfreight() {
		return lessfreight;
	}

	public void setLessfreight(Integer lessfreight) {
		this.lessfreight = lessfreight;
	}

	public Integer getLesscost() {
		return lesscost;
	}

	public void setLesscost(Integer lesscost) {
		this.lesscost = lesscost;
	}

	public Integer getMorefreight() {
		return morefreight;
	}

	public void setMorefreight(Integer morefreight) {
		this.morefreight = morefreight;
	}

	public Integer getMorecost() {
		return morecost;
	}

	public void setMorecost(Integer morecost) {
		this.morecost = morecost;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public Long getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(Long supplierid) {
		this.supplierid = supplierid;
	}

	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}
