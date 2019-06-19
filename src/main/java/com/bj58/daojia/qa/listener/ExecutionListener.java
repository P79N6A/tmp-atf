package com.bj58.daojia.qa.listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.StringUtils;
import org.testng.IClassListener;
import org.testng.IExecutionListener;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.IMethodInstance;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.alibaba.fastjson.JSON;
import com.bj58.daojia.qa.annotation.DSF;
import com.bj58.daojia.qa.annotation.Dubbo;
import com.bj58.daojia.qa.annotation.Http;
import com.bj58.daojia.qa.base.BaseDubboTest;
import com.bj58.daojia.qa.util.CoverageUtils;
import com.bj58.daojia.qa.util.DubboUtils;
import com.bj58.daojia.qa.util.InvokerUtils;
import com.bj58.daojia.qa.util.Logger;

/**
 * 监听执行过程
 * 
 * @author Nihuaiqing
 */
public class ExecutionListener extends TestListenerAdapter implements IExecutionListener, IInvokedMethodListener ,IClassListener {

	// 测试开始监听
	@Override
	public void onExecutionStart() {
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("                    测试执行开始");
		Logger.Defaultlog("=======================================================");
	}

	// 测试结束监听
	@Override
	public void onExecutionFinish() {
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("                    测试执行结束");
		Logger.Defaultlog("=======================================================");
		//关闭测试覆盖率
		CoverageUtils.stopCoverage();
	}

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		if (method.isTestMethod()) {// 只打印测试方法
			Logger.Defaultlog("=======================================================");
			Logger.Defaultlog("测试方法【  %s  】执行开始,测试数据Parameters=%s", method.getTestMethod().getMethodName(),
					JSON.toJSON(testResult.getParameters()));
			Logger.Defaultlog("=======================================================");
		}
	}

	public void initService(IMethodInstance result) {
	ITestNGMethod methods = result.getMethod();
	Object instance = methods.getInstance();
	Class instanceClass = instance.getClass();
		// 1、先查找field上包含注解的情况
		Field[] fs = instanceClass.getDeclaredFields();
		for (Field f : fs) {
			try {
				Annotation[] as = f.getDeclaredAnnotations();
				for (Annotation a : as) {
					if (a.annotationType().equals(DSF.class)) {
						DSF d = (DSF) a;
						String url = d.url();
						InvokerUtils.invokeDSFMethodAnnotation(url, f, instance);
					} else if (a.annotationType().equals(Dubbo.class)) {
						Dubbo dubbo = (Dubbo) a;
						String site = dubbo.value();
						if (StringUtils.isBlank(site)) {
							site = System.getProperty("Dubbo.Site");
//							Logger.Defaultlog("从config.properties加载集群名：%s", site);
						}
						InvokerUtils.invokeDubboMethodAnnotation(site, f, instance);
					} else if (a.annotationType().equals(Http.class)) {
						Http http = (Http) a;
						String site = http.site();
						String protocol = http.protocol();
						if (StringUtils.isBlank(site)) {
							site = System.getProperty("Http.Site");
//							Logger.Defaultlog("从config.properties加载集群名：%s", site);
						}
						if (StringUtils.isBlank(protocol)) {
							Logger.Defaultlog("未指定协议类型，默认使用http作为前缀");
						}
						InvokerUtils.invokeHttpMethodAnnotation(site, protocol, f, instance);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 2、查找class上包含dsf注解的情况
		Annotation[] as = instanceClass.getAnnotations();
		try {
			for (Annotation a : as) {
				if (a.annotationType().equals(DSF.class)) {
					DSF dsfAnnotation = (DSF) a;
					String url = dsfAnnotation.url();
					InvokerUtils.invokeDsfClassAnnotation(url, instance, instanceClass);
				} else if (a.annotationType().equals(Dubbo.class)) {
					Dubbo dubboAnnotation = (Dubbo) a;
					String site = dubboAnnotation.value();
					if (StringUtils.isBlank(site)) {
						site = System.getProperty("Dubbo.Site");
//						Logger.Defaultlog("从config.properties加载集群名：%s", site);
					}
					InvokerUtils.invokeDubboClassAnnotation(site, instance, instanceClass);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 3、继承basedubbotest类
		Class parentClass = instanceClass.getSuperclass();
		if (parentClass.getName().equals(BaseDubboTest.class.getName())) {
			try {
				String site = System.getProperty("Dubbo.Site");
				Object targetObject = DubboUtils.getAgent(InvokerUtils.getEntityClass(instanceClass), site);
				InvokerUtils.invokeClass(instance, instanceClass, targetObject);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
	}

	@Override
	public void onTestFailure(ITestResult tr) {
		super.onTestFailure(tr);
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("测试方法 【" + tr.getName() + "】 执行失败");
		Logger.Defaultlog("失败原因为：" + tr.getThrowable().getMessage());
		Logger.Defaultlog("=======================================================");
	}

	@Override
	public void onTestSkipped(ITestResult tr) {
		super.onTestSkipped(tr);
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("测试方法 【" + tr.getName() + "】 执行跳过");
		Logger.Defaultlog("=======================================================");
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		super.onTestSuccess(tr);
		Logger.Defaultlog("=======================================================");
		Logger.Defaultlog("测试方法 【" + tr.getName() + "】 执行成功！");
		Logger.Defaultlog("=======================================================");
	}

	public void onStart(ITestContext testContext) {
		super.onStart(testContext);
	}

	@Override
	public void onBeforeClass(ITestClass testClass, IMethodInstance mi) {
		initService(mi);
	}

	@Override
	public void onAfterClass(ITestClass testClass, IMethodInstance mi) {
		// TODO Auto-generated method stub
	}
	


}
