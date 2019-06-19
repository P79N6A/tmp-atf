package com.bj58.daojia.qa.util;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import com.bj58.daojia.qa.ci.EnvUtil;
import com.daojia.spat.dsf.client.DSFInit;
import com.daojia.spat.dsf.client.proxy.builder.DSFProxyFactory;

/**   
 * @Description: TODO
 * @author xh   
 * @date 2017年3月2日 下午2:43:52   
 */
public class DSFUtils {
	
	public static <T> T getService(Class<T> clazz, String serviceName) {
		String configpath = System.getProperty("DSF.ConfigPath", "dsf.config");
		configpath=EnvUtil.getDsfPath();
		String URL = "tcp://" + serviceName + "/" + clazz.getSimpleName().substring(1, clazz.getSimpleName().length());
		DSFInit.init(configpath);
		return DSFProxyFactory.create(clazz, URL);
	}

	public static <T> T getService(Class<T> clazz, String serviceName, String URL) {
		String configpath = System.getProperty("DSF.ConfigPath", "dsf.config");
		configpath=EnvUtil.getDsfPath();
		DSFInit.init(configpath);
		return DSFProxyFactory.create(clazz, URL);
	}

	public static <T> T getService(Class<T> clazz, String serviceName, String URL, String configpath) {
		if (StringUtils.isBlank(configpath)) {
			configpath = System.getProperty("DSF.ConfigPath", "dsf.config");
			configpath=EnvUtil.getDsfPath();
		}
		DSFInit.init(configpath);

		return DSFProxyFactory.create(clazz, URL);
	}
	
	public static <T> T getServices(Class<T> clazz, String URL, String configPath) {
		return getService(clazz, null, URL, configPath);
	}

	public static <T> T getServices(Class<T> clazz, String URL) {
		return getServices(clazz, URL, null);
	}
}
