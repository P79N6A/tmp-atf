package com.bj58.daojia.qa.base;

import java.io.File;
import java.lang.reflect.Method;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

import com.bj58.daojia.qa.ci.EnvUtil;
import com.bj58.daojia.qa.dataprovider.XmlDataProvider;
import com.bj58.daojia.qa.listener.ExecutionListener;
import com.bj58.daojia.qa.util.CoverageUtils;
import com.bj58.daojia.qa.util.InitProperties;
import com.bj58.daojia.qa.util.Logger;

/**
 * 基类
 * 
 * @author huaiq
 *
 * @param <T>
 */
@Listeners(ExecutionListener.class)
public abstract class BaseTest<T> {
	protected boolean isOffline = System.getProperty("isOffline", "true").equalsIgnoreCase("true") ? true : false;
	protected T baseAgent = null;
	private static final String DSFPATH = "resources"+File.separatorChar+"config"+File.separatorChar+"dsf.config";

	@BeforeSuite
	public void beforeSuite(){
		
	}
	
	// 初始化
	static {
		System.setProperty("DSF.ConfigPath", DSFPATH);
		// 初始化配置文件
		new InitProperties();
		// 初始化日志
		Logger.setLog();
		//重新设置测试环境
		EnvUtil.resetEnv();
		//
		CoverageUtils.startCoverage();
	}
	
	protected void setBaseAgent(T t){
		baseAgent=t;
	}

	/**
	 * xml数据驱动调用
	 */
	@DataProvider(name="xml")
	protected Object[][] xmlData(Method m) {
			return new XmlDataProvider().getData(m.getName(),m.getDeclaringClass().getSimpleName()+".xml");
	}
}
