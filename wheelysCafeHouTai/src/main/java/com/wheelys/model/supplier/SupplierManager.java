package com.wheelys.model.supplier;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class SupplierManager extends BaseObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4421457721727850910L;

	private Long id;
	private String suppname;
	private String delstatus;

	
	public SupplierManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public SupplierManager(Long id) {
		this.id = id;
		this.delstatus="N";
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSuppname() {
		return suppname;
	}

	public void setSuppname(String suppname) {
		this.suppname = suppname;
	}
	
	public String getDelstatus() {
		return delstatus;
	}


	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}


	@Override
	public Serializable realId() {
		// TODO Auto-generated method stub
		return null;
	}

}
