package com.wheelys.model;

import java.io.Serializable;

import com.wheelys.model.BaseObject;
import com.wheelys.util.DateUtil;

public class CafeRecruit extends BaseObject {
	public static final String CODE_SUCCESS = "0000";
	private static final long serialVersionUID = -7549609681641697224L;
	private Long id;//主键
	private String name;//名字
	private String sex;//性别
	private String idcard;//身份证
	private String phone;//电话
	private String wxid;//微信id
	private String email;//email
	private String company;//公司名称
	private String address;//公司地址
	private String companyphone;//公司电话
	private String values1;//第一意向
	private String values2;//第二意向
	private String values3;//第三意向
	private String values4;//关键资源能力
	private String values5;//未来发展
	private String values6;//最大难题
	private String values7;//希望wheelys支持
	private String values8;//是否同意wheelys20%
	private String values9;//投资wheelys产业基金
	private Long times;//时间
	private Integer state;//隐藏为0,显示为1
	
	public CafeRecruit() {
		
	}
	public CafeRecruit(String name) {
		this.name = name;
		this.times = DateUtil.getMillTimestamp().getTime()/1000;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCompanyphone() {
		return companyphone;
	}

	public void setCompanyphone(String companyphone) {
		this.companyphone = companyphone;
	}

	public String getValues1() {
		return values1;
	}

	public void setValues1(String values1) {
		this.values1 = values1;
	}

	public String getValues2() {
		return values2;
	}

	public void setValues2(String values2) {
		this.values2 = values2;
	}

	public String getValues3() {
		return values3;
	}

	public void setValues3(String values3) {
		this.values3 = values3;
	}

	public String getValues4() {
		return values4;
	}

	public void setValues4(String values4) {
		this.values4 = values4;
	}

	public String getValues5() {
		return values5;
	}

	public void setValues5(String values5) {
		this.values5 = values5;
	}

	public String getValues6() {
		return values6;
	}

	public void setValues6(String values6) {
		this.values6 = values6;
	}

	public String getValues7() {
		return values7;
	}

	public void setValues7(String values7) {
		this.values7 = values7;
	}

	public String getValues8() {
		return values8;
	}

	public void setValues8(String values8) {
		this.values8 = values8;
	}

	public String getValues9() {
		return values9;
	}

	public void setValues9(String values9) {
		this.values9 = values9;
	}

	public Long getTimes() {
		if(times == null)return 0L;
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public Serializable realId() {
	
		return id;
	}
	
}
