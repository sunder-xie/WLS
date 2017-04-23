package com.wheelys.service.order.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.order.MemberAddress;
import com.wheelys.service.order.MemberAddressService;
import com.wheelys.util.VmUtils;

@Service("memberAddressService")
public class MemberAddressServiceImpl extends BaseServiceImpl implements MemberAddressService {
	

	@Override
	public MemberAddress getMemberAddressByMemidAndShopid(Long memberid, Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(MemberAddress.class);
		query.add(Restrictions.eq("memberid", memberid));
		query.add(Restrictions.eq("shopid", shopid));
		List<MemberAddress> addresses = baseDao.findByCriteria(query);
		if(VmUtils.isEmptyList(addresses)){
			return null;
		}
		return addresses.get(0);
	}

	@Override
	public MemberAddress saveOrUpdateMemAddr(Long memberid, Long shopid, String username, String mobile, String detailaddress,
			String address) {
		MemberAddress memberAddress = this.getMemberAddressByMemidAndShopid(memberid, shopid);
		if(VmUtils.isEmpObj(memberAddress)){
			memberAddress = new MemberAddress();
			memberAddress.setAddress(address);
			memberAddress.setDetailaddress(detailaddress);
			memberAddress.setMemberid(memberid);
			memberAddress.setMobile(mobile);
			memberAddress.setShopid(shopid);
			memberAddress.setType("Y");
			memberAddress.setUsername(username);
			memberAddress.setCreatetime(DateUtil.getMillTimestamp());
			memberAddress.setUpdatetime(memberAddress.getCreatetime());
		}else{
			memberAddress.setAddress(address);
			memberAddress.setDetailaddress(detailaddress);
			memberAddress.setMobile(mobile);
			memberAddress.setUsername(username);
			memberAddress.setUpdatetime(DateUtil.getMillTimestamp());
		}
		baseDao.saveObject(memberAddress);
		return memberAddress;
	}

}
