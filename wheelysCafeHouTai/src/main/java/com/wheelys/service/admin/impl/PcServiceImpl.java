package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.CafeRecruit;
import com.wheelys.model.Pcemail;
import com.wheelys.service.admin.PcService;

@Service("pcService")
public class PcServiceImpl extends BaseServiceImpl implements PcService {

	@Override
	public CafeRecruit cafeRedcruit(String name, String sex, String idcard, String phone, String wxid, String email,
			String company, String address, String companyphone, String values1, String values2, String values3,
			String values4, String values5, String values6, String values7, String values8, String values9){
			boolean flag =this.findIdcard(idcard);
			CafeRecruit  cafeRecruit = new CafeRecruit(name);
			if(flag){
				CafeRecruit recruit=this.cafeRecruit(idcard);
				if(StringUtils.equals(name, recruit.getName())&&StringUtils.equals(sex, recruit.getSex())
						&&StringUtils.equals(idcard, recruit.getIdcard())&&StringUtils.equals(phone, recruit.getPhone())
						&&StringUtils.equals(wxid, recruit.getWxid())&&StringUtils.equals(email, recruit.getEmail())
						&&StringUtils.equals(company, recruit.getCompany())&&StringUtils.equals(address, recruit.getAddress())
						&&StringUtils.equals(companyphone, recruit.getCompanyphone())&&StringUtils.equals(values1,recruit.getValues1())
						&&StringUtils.equals(values2, recruit.getValues2())&&StringUtils.equals(values3, recruit.getValues3())
						&&StringUtils.equals(values4, recruit.getValues4())&&StringUtils.equals(values5, recruit.getValues5())
						&&StringUtils.equals(values6, recruit.getValues6())&&StringUtils.equals(values7, recruit.getValues7())
						&&StringUtils.equals(values8, recruit.getValues8())&&StringUtils.equals(values9, recruit.getValues9())
						){
					return baseDao.updateObject(recruit);
				}
			}
				cafeRecruit.setName(name);
				cafeRecruit.setSex(sex);
				cafeRecruit.setIdcard(idcard);
				cafeRecruit.setPhone(phone);
				cafeRecruit.setWxid(wxid);
				cafeRecruit.setEmail(email);
				cafeRecruit.setCompany(company);
				cafeRecruit.setAddress(address);
				cafeRecruit.setCompanyphone(companyphone);
				cafeRecruit.setValues1(values1);
				cafeRecruit.setValues2(values2);
				cafeRecruit.setValues3(values3);
				cafeRecruit.setValues4(values4);
				cafeRecruit.setValues5(values5);
				cafeRecruit.setValues6(values6);
				cafeRecruit.setValues7(values7);
				cafeRecruit.setValues8(values8);
				cafeRecruit.setValues9(values9);
				cafeRecruit.setState(1);
				return this.baseDao.saveObject(cafeRecruit);
	}
	
	@Override
	public Pcemail email(String email) {
		Date date = new Date();
		Integer time = (int) date.getTime();
		Pcemail pcemail = new Pcemail();
		pcemail.setEemail(email);
		pcemail.setEtime(time);
		return this.baseDao.saveObject(pcemail);
	}


	private boolean findIdcard(String idcard) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeRecruit.class);
		query.add(Restrictions.eq("idcard", idcard));
		query.setProjection(Projections.rowCount());
		List<Long> shopList = baseDao.findByCriteria(query);
		return shopList.get(0) > 0;
	}
	private CafeRecruit cafeRecruit(String idcard){
		DetachedCriteria query = DetachedCriteria.forClass(CafeRecruit.class);
		query.add(Restrictions.eq("idcard", idcard));
		List<CafeRecruit> cafeRecruitList = baseDao.findByCriteria(query);
		List<CafeRecruit>li = new ArrayList<>();
		for(CafeRecruit cafeRecruit:cafeRecruitList){
			li.add(cafeRecruit);
		}
		CafeRecruit recruit = li.get(0);
		return recruit;
		
	}
}
