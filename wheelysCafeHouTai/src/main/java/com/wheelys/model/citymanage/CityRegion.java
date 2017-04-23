package com.wheelys.model.citymanage;

public class CityRegion {
	private Long id;// id
	private String name;// 所属大区名字
	private String delstatus;// 删除状态

	public CityRegion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CityRegion(Long id, String name, String delstatus) {
		super();
		this.id = id;
		this.name = name;
		this.delstatus = delstatus;
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

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

}
