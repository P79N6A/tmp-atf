package com.bj58.daojia.qa.http;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bj58.daojia.qa.CommonConstant;
import com.bj58.daojia.qa.util.HttpUtils;

public class HttpRequestTester {
	private String PROTOCOL = null;
	private String IP = null;
	private String PORT = null;

	public HttpRequestTester(String protocol, String ip, String port) {
		super();
		PROTOCOL = protocol;
		IP = ip;
		PORT = port;
	}

	/**
	 * 通过Post请求返回Json格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @return
	 */
	public JSONObject doPostReturnResponseJson(String url) {
		return doPostReturnResponseJson(url, null, null, CommonConstant.UTF8);
	}

	/**
	 * 通过Post请求返回Json格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            请求参数
	 * @return
	 */
	public JSONObject doPostReturnResponseJson(String url, Map<String, String> params) {
		return doPostReturnResponseJson(url, params, null, CommonConstant.UTF8);
	}

	/**
	 * 通过raw body方式发送参数
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            请求参数
	 * @return
	 */
	public JSONObject doPostReturnResponseJson(String url, String params) {
		return doPostReturnResponseJson(url, params, null, null, null);
	}

	/**
	 * 通过Post请求返回Json格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            请求参数
	 * @param header
	 *            请求头
	 * @return
	 */
	public JSONObject doPostReturnResponseJson(String url, Map<String, String> params, Map<String, String> header) {
		return doPostReturnResponseJson(url, params, header, CommonConstant.UTF8);
	}

	/**
	 * 通过Post请求返回Json格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param params
	 *            请求参数
	 * @param header
	 *            请求头
	 * @param charset
	 *            字符编码
	 * @return
	 */
	public JSONObject doPostReturnResponseJson(String url, Map<String, String> params, Map<String, String> header,
			String charset) {
		return doPostReturnResponseJson(url, params, header, null, null);
	}

	// TODO
	public JSONObject doPostReturnResponseJson(String url, Map<String, String> params, Map<String, String> header,
			String filePath, String pwd) {

		url = HttpUtils.replaceProtocolIpPort(url, PROTOCOL, IP, PORT);
		return HttpRequest.doPostReturnResponseJson(url, params, header, filePath, pwd);
	}

	// TODO
	public JSONObject doPostReturnResponseJson(String url, String params, Map<String, String> header, String filePath,
			String pwd) {

		url = HttpUtils.replaceProtocolIpPort(url, PROTOCOL, IP, PORT);
		return HttpRequest.doPostReturnResponseJson(url, params, header,  filePath, pwd);
	}

	/**
	 * 通过POST发送请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @return
	 */
	public String doPostReturnResponse(String url) {
		return doPostReturnResponse(url, null, null);
	}

	/**
	 * 通过POST发送请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return
	 */
	public String doPostReturnResponse(String url, Map<String, String> params) {
		return doPostReturnResponse(url, params, null);
	}

	/**
	 * 通过POST发送请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param params
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	public String doPostReturnResponse(String url, Map<String, String> params, Map<String, String> header) {
		return doPostReturnResponse(url, params, header, null, null);
	}

	/**
	 * @Description: 通过POST发送请求 @param url @param params @param header @param
	 *               filePath @param pwd @param charset @return String @throws
	 */
	public String doPostReturnResponse(String url, Map<String, String> params, Map<String, String> header,
			String filePath, String pwd) {
		JSONObject jsonObject = doPostReturnResponseJson(url, params, header, filePath, pwd);
		return jsonObject.toJSONString();
	}

	/**
	 * 通过Get请求返回Josn格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @return
	 */
	public JSONObject doGetReturnResponseJson(String url) {
		return doGetReturnResponseJson(url, null);
	}

	/**
	 * 通过Get请求返回Josn格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param queryString
	 *            请求参数
	 * @return
	 */
	public JSONObject doGetReturnResponseJson(String url, String queryString) {
		return doGetReturnResponseJson(url, queryString, null);
	}

	/**
	 * 通过Get请求返回Josn格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param queryString
	 *            请求参数
	 * @param header
	 *            请求头
	 * @return
	 */
	public JSONObject doGetReturnResponseJson(String url, String queryString, Map<String, String> header) {
		return doGetReturnResponseJson(url, queryString, header, null, null);
	}

	/**
	 * 通过Get请求返回Josn格式对象
	 * 
	 * @param url
	 *            请求URL地址
	 * @param queryString
	 *            请求参数
	 * @param header
	 *            请求头
	 * @return
	 */
	public JSONObject doGetReturnResponseJson(String url, String queryString, Map<String, String> header, String file,
			String pwd) {

		url = HttpUtils.replaceProtocolIpPort(url, PROTOCOL, IP, PORT);
		return HttpRequest.doGetReturnResponseJson(url, queryString, header, file, pwd);
	}

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @return 返回请求响应的HTML
	 */
	public String doGetReturnResponse(String url) {
		return doGetReturnResponse(url, "", null);
	}

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @return 返回请求响应的HTML
	 */
	public String doGetReturnResponse(String url, String queryString) {
		return doGetReturnResponse(url, queryString, null);
	}

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param header
	 *            header信息
	 * @return 返回请求响应的HTML
	 */
	public String doGetReturnResponse(String url, String queryString, Map<String, String> header) {
		return doGetReturnResponse(url, queryString, header, null, null);
	}

	/**
	 * 执行一个HTTP GET请求，返回请求响应的HTML
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param queryString
	 *            请求的查询参数,可以为null
	 * @param header
	 *            header信息
	 * @param filePath
	 *            https的key文件路径
	 * @param pwd
	 *            秘钥key
	 * @return 返回请求响应的HTML
	 */
	public String doGetReturnResponse(String url, String queryString, Map<String, String> header, String filePath,
			String pwd) {
		JSONObject jsonObject = doGetReturnResponseJson(url, queryString, header, filePath, pwd);
		return jsonObject.toString();
	}

	public void main(String[] args) {
		System.out.println(HttpRequest.doGetReturnResponseJson("http://djoy.daojia-inc.com/tmp/getModuleList"));
	}

}
