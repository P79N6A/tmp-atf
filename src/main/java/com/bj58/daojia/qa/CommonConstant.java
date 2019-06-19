package com.bj58.daojia.qa;

/**   
 * @Description: TODO
 * @author xh   
 * @date 2017年2月28日 下午1:45:08   
 */
public class CommonConstant {
	
	//http连接池最大数量
	public static final int HTTPCLIENT_CONNECTION_COUNT=400;
	
	//单个路由最大连接数量
	public static final int HTTPCLIENT_MAXPERROUTE_COUNT=10000;
	
	//http连接最大重试次数
	public static final int HTTPCLIENT_MAXRETRY_COUNT=5;
	
	//连接超时时间
	public static final int HTTPCLIENT_CONNECT_TIMEOUT=10000;
	
	//socket超时时间
	public static final int HTTPCLIENT_SOCKET_TIMEOUT=10000;
	
	public static final String UTF8 = "UTF-8";
}
