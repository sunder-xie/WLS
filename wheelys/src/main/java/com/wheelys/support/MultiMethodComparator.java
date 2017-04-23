/**
 * 
 */
package com.wheelys.support;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.apache.commons.beanutils.PropertyUtils;

public class MultiMethodComparator<T> implements Comparator<T>{
	private String[] properties = null;
	private boolean[] asc;
	private int length = 0;
	
	public MultiMethodComparator(String[] properties, boolean[] asc){
		this.properties = properties;
		length = Math.min(properties.length, asc.length);
		this.asc = asc;
	}
	
	@Override
	public int compare(T o1, T o2) {
		int result = 0;
		if(o1!=null && o2==null){
			result = 1;
		}else if(o1==null && o2!=null){
			result = -1;
		}else if(o1!=null && o2!=null){
			for(int i=0; i< length; i++){
				Comparable p1 = null;
				Comparable p2 = null;
				try{
					try{
						p1 = (Comparable) PropertyUtils.getProperty(o1, properties[i]);
						p2 = (Comparable) PropertyUtils.getProperty(o2, properties[i]);
					}catch(NoSuchMethodException e){
						if(p1==null ){
							Class clazz = o1.getClass();
							Class clazz2 = o2.getClass();
							Method m1 = clazz.getDeclaredMethod(properties[i]);
							p1 = (Comparable) m1.invoke(o1);
							Method m2 = clazz2.getDeclaredMethod(properties[i]);
							p2 = (Comparable) m2.invoke(o2);
						}
					}
					if(p1==null && p2!=null) result = -1;
					else if(p2==null && p1!=null) result = 1;
					else if(p1!=null && p2!=null) result = p1.compareTo(p2);
					if(result!=0) return asc[i]? result: -result;
				}catch(Exception e){
					//ignore
				}
			}
		}
		return 0;
	}
}
