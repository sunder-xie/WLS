package com.wheelys.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wheelys.util.BaseWebUtils;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.OuterSorter;
import com.wheelys.util.WebLogger;
import com.wheelys.model.LatLngObject;

public class LatLngSortUtils {

	protected final static transient WheelysLogger dbLogger = WebLogger.getLogger(BaseWebUtils.class);

	private static double EARTH_RADIUS = 6371.393;  
    private static double rad(double d) {  
       return d * Math.PI / 180.0;  
    }

	public static <T> List<T> getShopListByNear(String lat, String lng, List<T> objList) {
		return getShopListByNear(lat, lng, objList, 0, false);
	}

	public static <T> List<T> getShopListByNear(String lat, String lng, List<T> objList, int distance,
			boolean validDistance) {
		if (StringUtils.isBlank(lat) || StringUtils.isBlank(lng)) {
			return objList;
		}
		OuterSorter sorter = new OuterSorter<LatLngObject>(false);
		for (T obj : objList) {
			LatLngObject sortObj = (LatLngObject) obj;
			String olat = sortObj.getLat();
			String olng = sortObj.getLng();
			if (StringUtils.isNotBlank(olat) && StringUtils.isNotBlank(olng)) {
				try {
					long length = Math.round(getDistance(Double.parseDouble(lat), Double.parseDouble(lng),
							Double.parseDouble(olat), Double.parseDouble(olng)));
					if (validDistance) {
						if (length < distance) {
							sorter.addBean(length, obj);
						}
					} else {
						sorter.addBean(length, obj);
					}
					sortObj.setDistance(length);
				} catch (Exception e) {
					dbLogger.warn("getSortKey:" + ((LatLngObject) obj).getSortKey(), e, 30);
				}
			}
		}
		return sorter.getAscResult();
	}
	
	/**
	 * 两点间距离
	 * @param longt1  经线
	 * @param lat1 纬线
	 * @param longt2
	 * @param lat2
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 1000);
		return s;
	}
}