package com.bj58.daojia.qa.util;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import com.bj58.daojia.qa.ci.EnvUtil;
import com.bj58.daojia.qa.http.HttpRequestTester;

/**
 * 
 * @author eqianyu
 *
 */
public class HttpUtils {

	/**
	 * 连接接口，并生成一个通用的调用接口的agent
	 * 
	 * @param agent
	 * @param env配置文件中对应的
	 *            env分组
	 * @return
	 */
	public static final <T> T getAgent(Class<T> agent, String site, String protocol) {
		String env = EnvUtil.getHttpEnvBySite(site);
		boolean isOffline = System.getProperty("isOffline", "true").equalsIgnoreCase("true") ? true : false;
		
		Assert.assertTrue(StringUtils.isNotEmpty(env), String.format("无法获取测试环境分组，请确保配置文件存在%s分组", "Http.ENV"));
		Logger.log("开始初始化%s agent,所属集群:%s,环境分组:%s",protocol,site,env);

		// 判断是否为线上环境
		isOffline = env.equalsIgnoreCase("alprod") || env.equalsIgnoreCase("online") ? false : true;
		System.setProperty("isOffline", String.valueOf(isOffline));

		// 获取测试机ip
		String ip = System.getProperty(String.format("Http.%s.%s.ip", site, env));
		Assert.assertTrue(StringUtils.isNotEmpty(ip),
				String.format("无法获取测试Server IP，请在配置文件中配置%s值", String.format("Http.%s.%s.ip", site, env)));
		// 获取服务端口
		String port = System.getProperty(String.format("Http.%s.%s.port", site, env),"");
		String url=String.format("%s://%s:%s/",protocol,ip,port);
		if(StringUtils.isEmpty(port)){
			url=String.format("%s://%s/",protocol,ip);
		}
		Logger.log("集群:%s,调用地址:%s",site,url);
		return (T) new HttpRequestTester(protocol, ip, port);
	}

	public static final <T> T getAgent(Class<T> agent, String site) {
		return getAgent(agent, site, "http");
	}



	public static void main(String[] args) {
		String url = "https://exmail.qq.com:8080/cgi-bin/frame_html?sid=bWx62muUTpBvoBNL,2&r=7a7dd488d4b9d2f87edd43a295e33785";

		url = "https://exmail.qq.com/cgi-bin/frame_html?sid=bWx62muUTpBvoBNL,2&r=7a7dd488d4b9d2f87edd43a295e33785";
	}

	public static String replaceProtocolIpPort(String url, String protocol, String ip, String port) {
		url=url.trim();
		protocol=protocol.trim();
		ip=ip.trim();
		port=port.trim();
		if(StringUtils.isNotEmpty(port)){
			port=":"+port;
		}
		if(url.startsWith("http:")||url.startsWith("https:")){
			Assert.fail("url 格式不正确,请参考：/api/getNameById,"+url);
		}
		if(!url.startsWith("/")){
			url="/"+url;
		}
		return protocol+"://"+ip+port+url;
	}
}