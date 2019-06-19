package com.bj58.daojia.qa.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.bj58.daojia.qa.container.EntityCache;

/**
 * 
 * @author eqianyu
 *
 */
public class InvokerUtils {

	public static <T> Class<T> getEntityClass(Class clazz) {
		Class<T> entityClass = null;
		Type t = clazz.getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			entityClass = (Class<T>) p[0];
		}
		return entityClass;
	}

	public static void invokeDSFMethodAnnotation(String url, Field f, Object instance)
			throws IllegalArgumentException, IllegalAccessException {
		Class ft = f.getType();
		f.setAccessible(true);
		if (f.get(instance) != null)
			return;

		String key = "dsf_" + ft.getName() + "_" + url;
		if (EntityCache.isExist(key)) {
			f.set(instance, EntityCache.get(key));
			Logger.Defaultlog("缓存读取dsf服务成功：%s", ft.getName());
		} else {
			Object oo = DSFUtils.getServices(ft, url);
			f.set(instance, oo);
			EntityCache.set(key, oo);
			Logger.Defaultlog("初始化dsf服务成功：%s", ft.getName());
		}
	}

	public static void invokeDubboMethodAnnotation(String site, Field f, Object instance)
			throws IllegalArgumentException, IllegalAccessException {
		Class ft = f.getType();
		f.setAccessible(true);
		if (f.get(instance) != null)
			return;

		String key = "dubbo_" + ft.getName() + "_" + site;
		if (EntityCache.isExist(key)) {
			f.set(instance, EntityCache.get(key));
			Logger.Defaultlog("缓存中读取dubbo服务成功：%s", ft.getName());
		} else {
			Object oo = DubboUtils.getAgent(ft, site);
			f.set(instance, oo);
			EntityCache.set(key, oo);
			Logger.Defaultlog("初始化dubbo服务成功：%s", ft.getName());
		}
	}

	// TODO
	public static void invokeHttpMethodAnnotation(String site, Field f, Object instance)
			throws IllegalArgumentException, IllegalAccessException {
		Class ft = f.getType();
		f.setAccessible(true);
		if (f.get(instance) != null) {
			return;
		}
		String key = "http_" + ft.getName() + "_" + site;
		if (EntityCache.isExist(key)) {
			f.set(instance, EntityCache.get(key));
			Logger.Defaultlog("缓存中读取集群%s http调用agent成功：%s", site);
		} else {
			Object oo = HttpUtils.getAgent(ft, site);
			f.set(instance, oo);
			EntityCache.set(key, oo);
			Logger.Defaultlog("初始化集群%s http调用agent成功", site);
		}
	}

	public static void invokeHttpMethodAnnotation(String site, String protocol, Field f, Object instance)
			throws IllegalArgumentException, IllegalAccessException {
		Class ft = f.getType();
		f.setAccessible(true);
		if (f.get(instance) != null) {
			return;
		}
		String key = protocol + "_" + ft.getName() + "_" + site;
		if (EntityCache.isExist(key)) {
			f.set(instance, EntityCache.get(key));
			Logger.Defaultlog("缓存中读取集群%s %s调用agent成功",site,protocol);
		} else {
			Object oo = HttpUtils.getAgent(ft, site, protocol);
			f.set(instance, oo);
			EntityCache.set(key, oo);
			Logger.Defaultlog("初始化集群%s %s调用agent成功",site,protocol);
		}
	}

	public static Object invokeDsfClassAnnotation(String url, Object instance, Class clazz)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object targetObject = null;
		String key = "dsf_" + getEntityClass(clazz).getName() + "_" + url;
		if (EntityCache.isExist(key)) {
			targetObject = EntityCache.get(key);
			invokeClass(instance, clazz, targetObject);
			return true;
		} else {
			targetObject = DSFUtils.getServices(getEntityClass(clazz), url);
		}

		invokeClass(instance, clazz, targetObject);
		EntityCache.set(key, targetObject);
		Logger.Defaultlog("初始化dsf服务成功：%s", getEntityClass(clazz).getName());
		return targetObject;
	}

	public static Object invokeDubboClassAnnotation(String site, Object instance, Class clazz)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object targetObject = null;
		String key = "dubbo_" + getEntityClass(clazz).getName() + "_" + site;
		if (EntityCache.isExist(key)) {
			targetObject = EntityCache.get(key);
			invokeClass(instance, clazz, targetObject);
			return true;
		} else {
			targetObject = DubboUtils.getAgent(getEntityClass(clazz), site);
		}

		invokeClass(instance, clazz, targetObject);
		EntityCache.set(key, targetObject);
		Logger.Defaultlog("初始化dubbo服务成功：%s", getEntityClass(clazz).getName());
		return targetObject;
	}

	public static Object invokeClass(Object instance, Class clazz, Object targetObject)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (Class<?> tempClazz = instance.getClass(); tempClazz != Object.class; tempClazz = tempClazz
				.getSuperclass()) {
			Method[] tempMethods = tempClazz.getDeclaredMethods();
			for (Method tempMethod : tempMethods) {
				if (tempMethod.getName().equals("setBaseAgent")) {
					tempMethod.setAccessible(true);
					tempMethod.invoke(instance, targetObject);
					return targetObject;
				}
			}
		}
		return null;
	}
}
