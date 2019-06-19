package com.bj58.daojia.qa.container;

import java.util.Hashtable;
import java.util.Map;

/**   
 * @Description: TODO
 * @author xh   
 * @date 2017年3月2日 下午3:45:46   
 */
public class EntityCache {
	
	private static Map<String,Object> currentCache=new Hashtable<String,Object>();
	
	public static boolean isExist(String key){
		synchronized (currentCache) {
			return currentCache.containsKey(key);
		}
	}
	
	public static void set(String key,Object value){
		synchronized (currentCache) {
			currentCache.put(key, value);
		}
	}
	
	public static Object get(String key){
		synchronized (currentCache) {
			return currentCache.get(key);
		}
	}
}
