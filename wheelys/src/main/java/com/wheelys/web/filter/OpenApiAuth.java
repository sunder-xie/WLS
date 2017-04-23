package com.wheelys.web.filter;

public class OpenApiAuth {
	
	public final static String MEMBERVO_KEY = "__member_vo_key__";
	
	private String apptype;			//app类型 ios、android
	private String appkey;			//appkey
	private String appversion;		//当前版本
	private String remoteip;		//远程调用ip
	private String memberencode;	//登录code
	
	public OpenApiAuth(){}
	
	public OpenApiAuth(String apptype, String appkey, String appversion, String remoteip, String memberencode){
		this.apptype = apptype;
		this.appkey = appkey;
		this.appversion = appversion;
		this.remoteip = remoteip;
		this.memberencode = memberencode;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppversion() {
		return appversion;
	}

	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}

	public String getRemoteip() {
		return remoteip;
	}

	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}

	public String getMemberencode() {
		return memberencode;
	}

	public void setMemberencode(String memberencode) {
		this.memberencode = memberencode;
	}
	
	
}
