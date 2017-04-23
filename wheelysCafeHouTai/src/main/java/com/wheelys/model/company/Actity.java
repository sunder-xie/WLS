package com.wheelys.model.company;

import java.io.Serializable;

import com.wheelys.model.BaseObject;

public class Actity extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 63636023735087659L;

	private Long id;
	private String actityinfo;
	

	public Actity() {
		
	}
	
	
	public Actity(String actityinfo) {
		this.actityinfo = actityinfo;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActityinfo() {
		return actityinfo;
	}

	public void setActityinfo(String actityinfo) {
		this.actityinfo = actityinfo;
	}

	@Override
	public Serializable realId() {

		return null;
	}

}
