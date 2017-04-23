package com.wheelys.model.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.wheelys.cons.MemberConstant;
import com.wheelys.model.acl.WheelysUser;
import com.wheelys.util.StringUtil;

public class WheelysMember extends WheelysUser {
	private static final long serialVersionUID = -5010141453720441090L;
	private Long id;
	private String nickname;
	private String headpic; // 头像
	private String email;
	private String password;
	private String mobile;
	private String rejected;
	private String bindStatus; // N：未绑定，X：未知，特殊用途，Y:绑定，Y_S：手机能通话验证过
	private String prikey; // 密钥
	private String needValid; // 黄牛可能性：Y 需要短信回复验证，N：不需要验证，U：暂不需要验证
	private long lastLoginTime; // 用户最后登录时间戳
	private Timestamp addtime;
	private String ip; 			//注册IP
	private String source;		//注册来源
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	private List<GrantedAuthority> tmpAuth;

	@Override
	public final List<GrantedAuthority> getAuthorities() {
		if (tmpAuth != null)
			return tmpAuth;
		tmpAuth = new ArrayList<GrantedAuthority>();
		tmpAuth.addAll(AuthorityUtils.createAuthorityList("member"));
		return tmpAuth;
	}

	@Override
	public final String getRolesString() {
		return "member";
	}

	@Override
	public final boolean isRole(String rolename) {
		return StringUtils.equals("member", rolename);
	}

	public WheelysMember() {
	}

	public WheelysMember(String nickname) {
		this.rejected = "N";
		this.nickname = nickname;
		this.bindStatus = "N";
		this.needValid = MemberConstant.NEEDVALID_U;
		if (StringUtils.isBlank(this.prikey)) {
			this.prikey = StringUtil.getRandomString(10);
		}
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRejected() {
		return rejected;
	}

	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	public String getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}

	@Override
	public boolean isEnabled() {
		return "N".equals(rejected);
	}

	@Override
	public Serializable realId() {
		return id;
	}

	@Override
	public String getRealname() {
		return this.nickname;
	}

	@Override
	public String getUsername() {
		return StringUtils.isBlank(email) ? mobile : email;
	}

	public String getSmobile() {
		if (StringUtils.isNotBlank(mobile) && mobile.length() == 11)
			return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
		return "";
	}

	@Override
	public String getUsertype() {
		return "member";
	}

	public boolean isBindMobile() {
		return StringUtils.isNotBlank(mobile) && StringUtils.startsWith(bindStatus, MemberConstant.BINDMOBILE_STATUS_Y);
	}

	public String getPrikey() {
		return prikey;
	}

	public void setPrikey(String prikey) {
		this.prikey = prikey;
	}

	public String getNeedValid() {
		return needValid;
	}

	public void setNeedValid(String needValid) {
		this.needValid = needValid;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Timestamp getAddtime() {
		return addtime;
	}

	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public String getHeadpic() {
		return headpic;
	}

	public void setHeadpic(String headpic) {
		this.headpic = headpic;
	}

	public String getHeadpicUrl() {
		if (StringUtils.isNotBlank(headpic))
			return headpic;
		return "img/default_head.png";
	}

}
