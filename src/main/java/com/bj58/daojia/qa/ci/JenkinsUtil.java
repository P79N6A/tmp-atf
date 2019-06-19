package com.bj58.daojia.qa.ci;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bj58.daojia.qa.util.Logger;
import com.bj58.daojia.test.http.HttpRequest;
import com.bj58.daojia.test.http.SampleResult;

public class JenkinsUtil {
	public static final boolean IS_ON_JENKINS=System.getenv().get("JENKINS_HOME") != null;
	public final String BUILD_URL=System.getenv().get("BUILD_URL") ;
	private static Map<String, String> jenkinsParaMap = null;

	
	public Map<String, String> getBuildParameters() {
		if(jenkinsParaMap==null){
			SampleResult result = null;
			HttpRequest httpRequest = new HttpRequest();
			Map<String, String> paraMap = new HashMap<String, String>();
			try {
				// 请求jenkisn接口获取构建参数
				result = httpRequest.get(String.format("%sapi/json", BUILD_URL), "");
				if (result.getContent() != null && !result.getContent().isEmpty()) {// 测试数据不为空
					JSONObject jsonResult = JSONObject.parseObject(result.getContent());
					JSONArray actionResult = JSONObject.parseArray(jsonResult.getString("actions"));
					if (actionResult != null && actionResult.size() > 0) {
						for (Object action : actionResult) {
							String parameters = JSONObject.parseObject(String.valueOf(action)).getString("parameters");
							if (parameters != null) {
								JSONArray parametersArray = JSONObject.parseArray(parameters);
								if (parametersArray != null && parametersArray.size() > 0) {
									for (Object parameter : parametersArray) {
										String key = JSONObject.parseObject(String.valueOf(parameter)).getString("name");
										String value = JSONObject.parseObject(String.valueOf(parameter)).getString("value");
										paraMap.put(key, value);
									}
								}
								break;
							}
						}
					}

				}
			} catch (Exception e) {
				Logger.Defaultlog("获取jenkins构建参数异常：");
				e.printStackTrace();
			}
			Logger.Defaultlog("获取jenkins构建参数为：%s",JSON.toJSONString(paraMap));
			jenkinsParaMap=paraMap;
		}
		return jenkinsParaMap;
	}
	
}
