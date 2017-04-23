package com.wheelys.service.admin.impl;


import org.springframework.stereotype.Service;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.supplier.SupplierManager;
import com.wheelys.service.admin.SupplierManagerService;
@Service("supplierManagerService")
public class SupplierManagerServiceImpl extends BaseServiceImpl implements SupplierManagerService {

	@Override
	public SupplierManager supplier(Long id) {
		SupplierManager supplier = this.baseDao.getObject(SupplierManager.class, id);
		return supplier;
	}

}
