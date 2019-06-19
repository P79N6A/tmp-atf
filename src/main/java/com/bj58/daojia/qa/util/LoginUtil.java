package com.bj58.daojia.qa.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bj58.daojia.qa.http.HttpRequestTester;
import com.google.common.base.Strings;

/**
 * * 登录 sso 登录 passport 登录 cpassport/custom
 * 
 */
public class LoginUtil {
	private static String cpassportType = "CPASSPORT";
	private static String passportType = "PASSPORT";
	private static String inpassType = "INPASS";
	private static String ssoType = "SSO";

	private static String getCookie = "cookies";
	private static String putCookie = "Cookie";

	/**
	 * 通过inpass登录 sso
	 * 
	 * @param userName
	 *            用户名 password 密码 roleId 角色ID roleUserName 角色名称
	 *            data:登录接口返回的userIds/username
	 * @return 用于下一次请求的header:JSESSIONID; _djinpasstkt; _djinpassuser;
	 *         acl_userId;acl_userRole; acl_username; acl_version; avl_token;
	 *         inpass;
	 * 
	 * 
	 */
	public static Map<String, String> inpassSSOLogin(final String userName, final String password, final String roleId,
			final String roleUserName, Map<String, Object> data) {
		String inpassip = getConfigParam(inpassType);
		Logger.log("配置参数inpass.ip: %s", inpassip);
		String ssoip = getConfigParam(ssoType);
		Logger.log("配置参数sso.ip: %s", ssoip);

		if (Strings.isNullOrEmpty(inpassip) || Strings.isNullOrEmpty(ssoip)) {
			return null;
		}

		final String redirect = "http://" + ssoip + "/index/index.do";
		String userAgent = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36";
		String accept = "application/json, text/javascript, */*; q=0.01";

		Map<String, String> header = new HashMap<String, String>();
		header.put("User-Agent", userAgent);
		header.put("Accept", accept);

		HttpRequestTester inpasshttp = new HttpRequestTester("http", inpassip, "");
		HttpRequestTester ssohttp = new HttpRequestTester("http", ssoip, "");
		JSONObject response = inpasshttp.doPostReturnResponseJson("/user/login", new HashMap<String, String>() {
			{
				put("userName", userName);
				put("password", password);
				put("redirect", redirect);
			}
		}, header);

		if (response.get(getCookie) != null && !Strings.isNullOrEmpty(response.get(getCookie).toString())) {
			JSONArray cookiearray = JSON.parseArray(response.get(getCookie).toString());
			String cookieVal = getcookievalue(cookiearray);
			if (!Strings.isNullOrEmpty(cookieVal)) {
				header.put(putCookie, cookieVal);
				if (data != null && response.get("inPassloginUser") != null) {
					JSONObject datajs = (JSONObject) response.get("inPassloginUser");
					for (Entry<String, Object> entry : datajs.entrySet()) {
						data.put(entry.getKey(), entry.getValue());
					}
				}

				JSONObject response2 = ssohttp.doPostReturnResponseJson("/login/switchuser.do",
						new HashMap<String, String>() {
							{
								put("id", roleId);
								put("username", roleUserName);
							}
						}, header);

				if (response2.get(getCookie) != null && !Strings.isNullOrEmpty(response2.get(getCookie).toString())) {
					JSONArray cookiearray2 = JSON.parseArray(response2.get(getCookie).toString());
					String cookieVal2 = getcookievalue(cookiearray2);
					if (!Strings.isNullOrEmpty(cookieVal2)) {
						header.put(putCookie, cookieVal + cookieVal2);
						if (data != null && response2.get("data") != null) {
							JSONObject datajs = (JSONObject) response2.get("data");
							for (Entry<String, Object> entry : datajs.entrySet()) {
								data.put(entry.getKey(), entry.getValue());
							}
						}

						return header;
					}
				}
			}
		}

		return null;

	}

	/**
	 * 登录 用户端passport
	 * 
	 * @param cityid
	 *            城市;code 验证码; mobile 电话;
	 * 
	 * @return 用于下一次请求的header: dj_pstoken; dj_pstokenexp; dj_psuid; djfrtexp;
	 *         djfrttok;djfrtuid; phone; uid;
	 * 
	 */
	public static Map<String, String> passportLogin(final String cityid, final String code, final String mobile) {
		String userAgent = "Mozilla/5.0 (Linux; Android 5.1.1; OPPO R7sm Build/LMY47V; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/42.0.2311.138 Mobile Safari/537.36 58app cdvsupport";
		String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
		String context = "application/x-www-form-urlencoded";
		String encoding = "gzip, deflate";
		String language = "zh-CN,en-US;q=0.8";
		String xRequestedWith = "XMLHttpRequest";

		Map<String, String> header = new HashMap<String, String>();
		header.put("User-Agent", userAgent);
		header.put("Accept", accept);
		header.put("Content-Type", context);
		header.put("Accept-Encoding", encoding);
		header.put("Accept-Language", language);
		header.put("X-Requested-With", xRequestedWith);

		String passportip = getConfigParam(passportType);
		Logger.log("配置参数passport.ip: %s", passportip);

		if (Strings.isNullOrEmpty(passportip)) {
			return null;
		}

		HttpRequestTester passporthttp = new HttpRequestTester("http", passportip, "");
		JSONObject response = passporthttp.doPostReturnResponseJson("/mobile/login", new HashMap<String, String>() {
			{
				put("cityid", cityid);
				put("code", code);
				put("mobile", mobile);
			}

		}, header);

		if (response.get(getCookie) != null && !Strings.isNullOrEmpty(response.get(getCookie).toString())) {
			JSONArray cookiearray = JSON.parseArray(response.get(getCookie).toString());
			String cookieVal = getcookievalue(cookiearray);
			if (!Strings.isNullOrEmpty(cookieVal)) {
				header.put(putCookie, cookieVal);

				return header;
			}
		}

		return null;
	}

	/**
	 * 登录商家passport
	 * 
	 * @param serviceId
	 *            服务ID;source 服务名称;code 验证码; phone 电话;
	 *            data:登录接口返回response中的data.intime=;address;lng;
	 *            mobile;dianzhangtel;photo;
	 *            sellerState=;cityid=;djcpasstoken=;djcpassexp;customId;djcpassecustomid;
	 *            name;lat;
	 * 
	 * @return 用于下一次请求的header:djcpassappid; djcpassappversion; djcpassimei;
	 *         djcpassappid; djcpassappversion; djcpassecustomid; djcpassexp;
	 *         djcpassimei; djcpassserviceid; djcpasssource; djcpasstoken;
	 * 
	 * 
	 * 
	 */
	public static Map<String, String> cpassportCustomLogin(final String serviceId, final String source,
			final String code, final String phone, Map<String, Object> data) {
		Map<String, String> header = new HashMap<String, String>();
		String cpassportip = getConfigParam(cpassportType);
		Logger.log("配置参数cpassport.ip: %s", cpassportip);

		if (Strings.isNullOrEmpty(cpassportip)) {
			return null;
		}

		HttpRequestTester passporthttp = new HttpRequestTester("http", cpassportip, "");
		JSONObject response = passporthttp.doPostReturnResponseJson("/custom/setcookie");
		if (response.get(getCookie) != null && !Strings.isNullOrEmpty(response.get(getCookie).toString())) {
			JSONArray cookiearray = JSON.parseArray(response.get(getCookie).toString());
			String cookieVal = getcookievalue(cookiearray);
			if (!Strings.isNullOrEmpty(cookieVal)) {
				header.put(putCookie, cookieVal);
				JSONObject response2 = passporthttp.doPostReturnResponseJson("/custom/login",
						new HashMap<String, String>() {
							{
								put("serviceId", serviceId);
								put("source", source);
								put("code", code);
								put("phone", phone);
							}
						}, header);
				if (response2.get(getCookie) != null && !Strings.isNullOrEmpty(response2.get(getCookie).toString())) {
					JSONArray cookiearray2 = JSON.parseArray(response2.get(getCookie).toString());
					String cookieVal2 = getcookievalue(cookiearray2);
					if (!Strings.isNullOrEmpty(cookieVal2)) {
						header.put(putCookie, cookieVal2);
						if (data != null && response2.get("data") != null) {
							JSONObject datajs = (JSONObject) response2.get("data");
							for (Entry<String, Object> entry : datajs.entrySet()) {
								data.put(entry.getKey(), entry.getValue());
							}
						}
						return header;
					}

				}

			}
		}
		return null;

	}

	/**
	 * 
	 * @param cookies
	 * @return 往header中放的Cookie字符串
	 */
	private static String getcookievalue(JSONArray cookies) {
		String cookievalue = "";
		for (Object object : cookies) {
			JSONObject cookie = JSONObject.parseObject(object.toString());
			if ((!Strings.isNullOrEmpty(cookie.getString("name")))
					&& (!Strings.isNullOrEmpty(cookie.getString("value")))) {
				cookievalue += (cookie.getString("name") + "=" + cookie.getString("value") + ";");

			}
		}
		return cookievalue;
	}

	/**
	 * 
	 * @param type
	 * @return 配置参数
	 */
	private static String getConfigParam(String type) {
		String env = System.getProperty("Http.ENV");
		String ip = System.getProperty(String.format("Http.%s.%s.ip", type, env));
		if (!Strings.isNullOrEmpty(ip)) {
			return ip;
		}
		return "";
	}

}
