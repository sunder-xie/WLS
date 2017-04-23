package com.wheelys.web.action.test;

import com.wheelys.util.Gcache;
import com.wheelys.util.StringUtil;

public class TestController2 {

	public final static Gcache<String,String> gcache = new Gcache<String,String>(2);
	
	public static void main(String[] args) {
		System.out.println(StringUtil.md5("r-uf6bb9behouttaite88^stf$%$&5l*((12r1xtKspCmcqXb3en214iu"));
	}
	
}
