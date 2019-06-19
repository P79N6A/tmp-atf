package com.bj58.daojia.qa.util;

import org.testng.Assert;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.bj58.daojia.qa.ci.EnvUtil;

/**   
 * @Description: TODO
 * @author xh   
 * @date 2017年3月2日 下午7:07:47   
 */
public class DubboUtils {

	/**
	 * 连接接口，并生成一个通用的调用接口的agent
	 * @param agent
	 * @param env配置文件中对应的 env分组
	 * @return
	 */
	public static final <T> T getAgent(Class<T> agent,String site){
		String env = EnvUtil.getDubboEnvBySite(site);
		boolean isOffline = System.getProperty("isOffline", "true").equalsIgnoreCase("true") ? true : false;
		
		Assert.assertTrue(StringUtils.isNotEmpty(env), String.format("无法获取测试环境分组，请确保配置文件存在%s分组", "Dubbo.ENV"));
		Logger.log("开始初始化dubbo agent:%s,所属集群:%s,环境分组:%s",agent.getName(),site,env);
		ApplicationConfig application = new ApplicationConfig();
		//判断是否为线上环境
		isOffline=env.equalsIgnoreCase("alprod")||env.equalsIgnoreCase("online")?false:true;
		System.setProperty("isOffline", String.valueOf(isOffline));
		application.setName("tmp-framework");
		ReferenceConfig<T> reference = new ReferenceConfig<T>();
		reference.setApplication(application);
		//获取测试机ip
		String ip=System.getProperty(String.format("Dubbo.%s.%s.ip",site,env));
		Assert.assertTrue(StringUtils.isNotEmpty(ip),String.format("无法获取测试Server IP，请在配置文件中配置%s值",String.format("Dubbo.%s.%s.ip",site,env)));
		//获取服务端口
		String port=System.getProperty(String.format("Dubbo.%s.%s.port",site,env));
		Assert.assertTrue(StringUtils.isNotEmpty(port),String.format("无法获取测试Server 端口，请在配置文件中配置%s值",String.format("Dubbo.%s.%s.port",site,env)));
		//设置调用地址
		reference.setUrl(String.format("dubbo://%s:%s/",ip,port));
		Logger.log("集群:%s,调用地址:%s",site,String.format("dubbo://%s:%s/",ip,port));
		//设置版本
		String version= System.getProperty("Dubbo.Version");
		reference.setVersion(StringUtils.isNotEmpty(version)?version:"1.0.0");
		//设置超时时间
		reference.setTimeout(10000);
		//设置调用agent
		reference.setInterface(agent.getName());
		return reference.get();
	}
}
