package com.bj58.daojia.qa.container;

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.testng.Assert;

/**   
 * @author nihuaiqing  
 */
public class DataCache {
	
	private static Map<String,Object> currentCache=new Hashtable<String,Object>();
	private static final String SEPARATOR="-";
	
	public static boolean isExist(Class className,String key){
		key = getKey(className,key);
		synchronized (DataCache.class) {
			return currentCache.containsKey(key);
		}
	}
	/**
	 * key值不能重复，如果已经存在执行时抛异常
	 * @param className
	 * @param key
	 * @param value
	 */
	public static void add(Class className,String key,Object value){
		if(value==null){
			Assert.fail("value值不能为空");
		}
		key = getKey(className,key);
		synchronized (DataCache.class) {
			if(currentCache.containsKey(key)){
				Assert.fail(String.format("类%s中已经存在key:%s,请换用其它key",className.getName(),key));
			}
			currentCache.put(key, value);
		}
	}
	
	public static Object get(Class className,String key){
		key = getKey(className,key);
		return currentCache.get(key);
	}
	
	public static void remove(Class className,String key){
		key = getKey(className,key);
		currentCache.remove(key);
	}
	
	/**
	 * key 如果存在，更新value值；不存在 存放value值
	 * @param className
	 * @param key
	 * @param value
	 */
	public static void update(Class className,String key,Object value){
		key = getKey(className,key);
		currentCache.put(key, value);
	}
	
	private static String getKey(Class<T> className,String key){
		if(StringUtils.isEmpty(key)){
			Assert.fail("key值不能为空");
		}
		return className.getName()+SEPARATOR+key;
	}
	
}
