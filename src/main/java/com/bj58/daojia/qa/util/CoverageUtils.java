package com.bj58.daojia.qa.util;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.bj58.daojia.qa.ci.JenkinsUtil;

public class CoverageUtils {
	public static final String COVERAGE_URL = "http://coverage.daojia-inc.com/";

	/**
	 * 开启测试覆盖率
	 */
	public static void startCoverage() {
		try {
			String doCoverage = System.getProperty("doCoverage", "false").trim();
			if (JenkinsUtil.IS_ON_JENKINS && doCoverage.equalsIgnoreCase("true")) {// Jenkins上执行
				JenkinsUtil jenkinsUtil = new JenkinsUtil();
				Map<String, String> paraMap = jenkinsUtil.getBuildParameters();
				if (paraMap != null && paraMap.size() > 0) {
					String server = paraMap.get("Server");
					String site = paraMap.get("Site");
					String email = paraMap.get("Email");
					if (StringUtils.isNotEmpty(server) && StringUtils.isNotEmpty(site)
							&& StringUtils.isNotEmpty(email)) {
						Logger.Defaultlog("开始开启测试覆盖率");
						// 清空测试覆盖率数据
						// get(COVERAGE_URL + "api/resetCoverage?",
						// String.format("serverIp=%s&clusterName=%s&email=%s", server, site, email));
						// 开启测试覆盖率
						String result = get(COVERAGE_URL + "api/startCoverage?",
								String.format("serverIp=%s&clusterName=%s&email=%s", server, site, email));
						Thread.sleep(60000);
						if (StringUtils.isNotEmpty(result) && Boolean.valueOf(result)) {
							Logger.Defaultlog("开启测试覆盖率成功,集群名:%s,服务器:%s", site, server);
						} else {
							Logger.Defaultlog("开启测试覆盖率超时,集群名:%s,服务器:%s", site, server);
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.Defaultlog("开启测试覆盖率异常:");
			e.printStackTrace();
		}
	}

	/**
	 * 关闭测试覆盖率
	 */
	public static void stopCoverage() {
		try {
			String doCoverage = System.getProperty("doCoverage", "false").trim();
			if (JenkinsUtil.IS_ON_JENKINS && doCoverage.equalsIgnoreCase("true")) {// Jenkins上执行
				JenkinsUtil jenkinsUtil = new JenkinsUtil();
				Map<String, String> paraMap = jenkinsUtil.getBuildParameters();
				if (paraMap != null && paraMap.size() > 0) {
					String server = paraMap.get("Server");
					String site = paraMap.get("Site");
					String email = paraMap.get("Email");
					if (StringUtils.isNotEmpty(server) && StringUtils.isNotEmpty(site)
							&& StringUtils.isNotEmpty(email)) {
						Logger.Defaultlog("开始关闭测试覆盖率");
						// 关闭测试覆盖率
						String result = get(COVERAGE_URL + "api/stopCoverage?",
								String.format("serverIp=%s&clusterName=%s&email=%s", server, site, email));
						if (StringUtils.isNotEmpty(result) && Boolean.valueOf(result)) {
							Logger.Defaultlog("关闭测试覆盖率成功,集群名:%s,服务器:%s", site, server);
						} else {
							Logger.Defaultlog("关闭测试覆盖率失败,集群名:%s,服务器:%s", site, server);
						}
					}
				}
			}
		} catch (Exception e) {
			Logger.Defaultlog("关闭测试覆盖率异常:");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			String result = get(COVERAGE_URL + "api/stopCoverage?", String.format("serverIp=%s&clusterName=%s&email=%s",
					"10.37.18.187", "dianshangwuxian_tmp_deploy", "eqianyu@daojia.com"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义get方法,设置超时时间
	 * 
	 * @param url
	 * @param para
	 * @return
	 * @throws Exception
	 */
	private static String get(String url, String para) throws Exception {
		String result = "";
		url = url.trim();
		url = url.endsWith("?") ? url : url + "?";
		url = (para != null) ? url + para : url;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			// 创建httpget.
			HttpGet httpget = new HttpGet(url);
			httpget.setConfig(RequestConfig.custom().setSocketTimeout(300000).setConnectionRequestTimeout(300000)
					.setConnectTimeout(5000).build());
			// 执行get请求.
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				// 获取响应实体
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					result = EntityUtils.toString(entity);
				}
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
