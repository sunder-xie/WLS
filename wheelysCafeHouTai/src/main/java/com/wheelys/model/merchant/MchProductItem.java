package com.wheelys.model.merchant;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

/**
 * 商家进货类型
 */
public class MchProductItem extends BaseObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7260350946558912703L;
	private Long id;
	private String name; // 类别名称
	private String status; // 是否显示状态Y/N
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
