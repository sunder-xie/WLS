package com.wheelys.service;

public interface OperationService {
	/**
	 * 每次操作必须间隔allowIntervalSecond
	 * @param opkey
	 * @param allowIntervalSecond 时间间隔（秒）
	 * @return
	 */
	boolean isAllowOperation(String opkey, int allowIntervalSecond);
	boolean updateOperation(String opkey, int allowIntervalSecond);
	/**
	 * scopeSecond这段时间内（秒）最多允许操作allowNum次
	 * @param opkey
	 * @param scopeSecond 时间范围（秒）
	 * @return
	 */
	boolean isAllowOperation(String opkey, int scopeSecond, int allowNum);
	boolean updateOperation(String opkey, int scopeSecond, int allowNum);
	/**
	 * 每次操作必须间隔allowIntervalSecond且scopeSecond这段时间内（秒）最多允许操作allowNum次
	 * implied condition: allowIntervalSecond * allowNum < scopeSecond
	 * @param opkey
	 * @param allowIntervalSecond 时间间隔（秒）
	 * @param scopeSecond 时间范围（秒）
	 * @param allowNum
	 * @return
	 */
	boolean isAllowOperation(String opkey, int allowIntervalSecond, int scopeSecond, int allowNum);
	boolean updateOperation(String opkey, int allowIntervalSecond, int scopeSecond, int allowNum);
	/**
	 * 重置时间间隔
	 * @param opkey
	 * @param secondNum
	 */
	void resetOperation(String opkey, int secondNum);
}
