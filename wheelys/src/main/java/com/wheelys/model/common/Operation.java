package com.wheelys.model.common;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wheelys.model.BaseObject;

public class Operation extends BaseObject{
	
	private static final long serialVersionUID = 7953185925619434267L;
	
	private String opkey;
	private Integer opnum;									//操作次数
	private Integer refused;								//拒绝次数
	private Timestamp addtime;								//增加时间
	private Timestamp updatetime;							//最后一次时间	
	private Timestamp validtime;							//有效时间
	private String tag;										//分类
	
	public Operation(){}
	public Operation(String opkey, Timestamp addtime){
		this.opkey = opkey;
		this.refused = 0;
		this.opnum = 1;
		this.addtime = addtime;
		this.updatetime = addtime;
	}
	public Operation(String opkey, Timestamp addtime, String tag){
		this(opkey, addtime);
		this.tag = tag;
	}
	public Operation(String opkey, Timestamp addtime, Timestamp validtime, String tag){
		this.opkey = opkey;
		this.refused = 0;
		this.opnum = 1;
		this.addtime = addtime;
		this.updatetime = addtime;
		this.validtime = validtime;
		this.tag = tag;
	}
	public Integer getOpnum() {
		return opnum;
	}
	public void setOpnum(Integer opnum) {
		this.opnum = opnum;
	}
	public Timestamp getAddtime() {
		return addtime;
	}
	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}
	public Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getOpkey() {
		return opkey;
	}
	public void setOpkey(String opkey) {
		this.opkey = opkey;
	}
	@Override
	public Serializable realId() {
		return opkey;
	}
	public Timestamp getValidtime() {
		return validtime;
	}
	public void setValidtime(Timestamp validtime) {
		this.validtime = validtime;
	}
	public Integer getRefused() {
		return refused;
	}
	public void setRefused(Integer refused) {
		this.refused = refused;
	}
}
