package com.wheelys.json;

/**
 * @author Administrator 支付宝查询用户对象
 */
public class AliPayUserVo {

	private String user_id;   // 支付宝用户的userId
	private String nick_name; // 用户昵称
	private String avatar;
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}


	public AliPayUserVo() {
	}

	@Override
	public String toString() {
		return "AliPayUserVo [user_id=" + user_id + ", nick_name=" + nick_name + ", avatar=" + avatar + "]";
	}

	public AliPayUserVo(String user_id, String nick_name, String avatar) {
		super();
		this.user_id = user_id;
		this.nick_name = nick_name;
		this.avatar = avatar;
	}


}
