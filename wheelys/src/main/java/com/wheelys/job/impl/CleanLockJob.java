package com.wheelys.job.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.wheelys.job.JobService;
import com.wheelys.util.DateUtil;

public class CleanLockJob extends JobService{
	@Autowired@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	public void cleanJoblock(){
		String delete = "delete FROM joblock where firetime < ? and status='Y'";
		String date =  DateUtil.format(DateUtil.addDay(new Date(), -3), "yyyyMMddHHmmss");
		int count = jdbcTemplate.update(delete, date);
		dbLogger.warnWithType("LogTypeConstant.LOG_TYPE_JOB", "cleanJobLock:" + count);
	}
}
