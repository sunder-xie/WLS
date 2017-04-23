package com.wheelys.model;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class CafeItem extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8389250934762521104L;
	
	private Long id; // id
	private String name; // 类别名称
	private String state; // 是否显示状态Y/N
	private Integer sortkey;// 排序key
	private Timestamp createtime;// 创建时间
	private Timestamp updatetime;// 更新时间

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	@Override
	public Serializable realId() {
		return id;
	}

}
