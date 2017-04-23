package com.wheelys.service.admin;

import com.wheelys.model.CafeRecruit;
import com.wheelys.model.Pcemail;

public interface PcService {
	
	
	
	/**
	 * 
	 * @param name
	 * @param sex
	 * @param idcard
	 * @param phone
	 * @param wxid
	 * @param email
	 * @param company
	 * @param address
	 * @param companyphone
	 * @param values1
	 * @param values2
	 * @param values3
	 * @param values4
	 * @param values5
	 * @param values6
	 * @param values7
	 * @param values8
	 * @param values9
	 * @return报名表新增
	 */
	CafeRecruit cafeRedcruit (String name,String sex,
			String idcard,String phone,String wxid,String email,String company,String address,
			String companyphone,String values1,String values2,String values3,String values4,String values5,
			String values6,String values7,String values8,String values9);
	
	Pcemail email(String email);
}
