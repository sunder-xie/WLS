package com.wheelys.service.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.common.Operation;
import com.wheelys.service.OperationService;

@Service("operationService")
public class OperationServiceImpl extends BaseServiceImpl implements OperationService {
	
	@Override
	public boolean isAllowOperation(String opkey, int allowIntervalSecond) {
		Operation op = baseDao.getObject(Operation.class, opkey);
		Timestamp cur = new Timestamp(System.currentTimeMillis());
		if(op != null){
			double diff = DateUtil.getDiffSecond(cur, op.getUpdatetime());
			if(diff < allowIntervalSecond){//间隔小于allowIntervalSecond，不允许
				return false;
			}
		}
		return true;
	}
	@Override
	public boolean updateOperation(String opkey, int allowIntervalSecond){
		Operation op = baseDao.getObject(Operation.class, opkey);
		Timestamp cur = new Timestamp(System.currentTimeMillis());
		boolean allow = true;
		if(op != null){
			double diff = DateUtil.getDiffSecond(cur, op.getUpdatetime());
			if(diff < allowIntervalSecond){//间隔小于allowIntervalSecond，不允许
				allow = false;
				op.setRefused(op.getRefused()+1);
			}else{
				op.setRefused(0);
				op.setUpdatetime(cur);
			}
		}else{
			op = new Operation(opkey, cur);
		}
		op.setValidtime(DateUtil.addSecond(cur, allowIntervalSecond));
		baseDao.saveObject(op);
		return allow;
	}
	@Override
	public boolean isAllowOperation(String opkey, int scopeSecond, int allowNum) {
		Operation op = baseDao.getObject(Operation.class, opkey);
		Timestamp cur = new Timestamp(System.currentTimeMillis());
		if(op != null){
			double diff = DateUtil.getDiffSecond(cur, op.getAddtime());
			if(diff < scopeSecond){
				if(op.getOpnum() >= allowNum) return false;
			}
		}
		return true;
	}
	@Override
	public boolean updateOperation(String opkey, int scopeSecond, int allowNum) {
		Operation op = baseDao.getObject(Operation.class, opkey);
		Timestamp cur = new Timestamp(System.currentTimeMillis());
		boolean allow = true;
		if(op != null){
			double diff = DateUtil.getDiffSecond(cur, op.getAddtime());
			if(diff < scopeSecond){
				if(op.getOpnum() >= allowNum) {
					allow = false;
				}
			}else{
				op.setAddtime(cur);//重新来过
				op.setValidtime(DateUtil.addSecond(cur, scopeSecond));
				op.setOpnum(0);
			}
			if(allow) {
				op.setUpdatetime(cur);
				op.setOpnum(op.getOpnum()+1);
				op.setRefused(0);
			}else{
				op.setRefused(op.getRefused()+1);
			}
		}else{
			op = new Operation(opkey, cur);
			op.setValidtime(DateUtil.addSecond(cur, scopeSecond));
		}
		baseDao.saveObject(op);
		return allow;
	}

	@Override
	public boolean isAllowOperation(String opkey, int allowIntervalSecond, int scopeSecond, int allowNum) {
		Operation op = baseDao.getObject(Operation.class, opkey);
		Timestamp cur = new Timestamp(System.currentTimeMillis());
		if(op != null){
			double diff = DateUtil.getDiffSecond(cur, op.getAddtime());
			if(diff < scopeSecond){
				if(op.getOpnum() >= allowNum) {
					return false;
				}else{
					double diff2 = DateUtil.getDiffSecond(cur, op.getUpdatetime());
					if(diff2 < allowIntervalSecond){//间隔小于allowIntervalSecond，不允许
						return false;
					}					
				}
			}
		}
		return true;
	}
	@Override
	public boolean updateOperation(String opkey, int allowIntervalSecond, int scopeSecond, int allowNum) {
		Operation op = baseDao.getObject(Operation.class, opkey);
		Timestamp cur = new Timestamp(System.currentTimeMillis());
		boolean allow = true;
		if(op != null){
			double diff = DateUtil.getDiffSecond(cur, op.getAddtime());
			if(diff < scopeSecond){
				if(op.getOpnum() >= allowNum) {
					allow = false;
				}else{
					double diff2 = DateUtil.getDiffSecond(cur, op.getUpdatetime());
					if(diff2 < allowIntervalSecond){//间隔小于allowIntervalSecond，不允许
						allow = false;
					}
				}
			}else{
				op.setAddtime(cur);//重新来过
				op.setValidtime(DateUtil.addSecond(cur, scopeSecond));
				op.setOpnum(0);
			}
			if(allow) {
				op.setUpdatetime(cur);
				op.setOpnum(op.getOpnum()+1);
				op.setRefused(0);
			}else{
				op.setRefused(op.getRefused()+1);
			}
		}else{
			op = new Operation(opkey, cur);
			op.setValidtime(DateUtil.addSecond(cur, scopeSecond));
		}
		baseDao.saveObject(op);
		return allow;
	}
	@Override
	public void resetOperation(String opkey, int secondNum){
		Operation op = baseDao.getObject(Operation.class, opkey);
		if(op!=null){
			Timestamp cur = new Timestamp(System.currentTimeMillis());
			op.setUpdatetime(DateUtil.addSecond(cur, -secondNum));
			baseDao.saveObject(op);
		}
	}

}
