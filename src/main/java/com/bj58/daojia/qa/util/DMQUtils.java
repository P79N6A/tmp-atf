package com.bj58.daojia.qa.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.bj58.daojia.qa.http.HttpRequest;


public class DMQUtils {
	public static final String URL="http://dmqsend.djtest.cn/second/msgsend";
	
	/**
	 * 发送dmq消息:
	 * 	1. 只支持dmq2.0, 2. 只支持测试环境发送
	 * @param topic 消息主题号
	 * @param body 消息体
	 * @return 发送成功 true,发送失败 false
	 */
	public static Boolean send(String topic,String body){
		if(StringUtils.isNotBlank(topic)&&StringUtils.isNotBlank(body)){
			Map<String,String> paraMap =new HashMap<String,String>();
			paraMap.put("topic", topic);
			paraMap.put("body", body);
			try{
				JSONObject result = HttpRequest.doPostReturnResponseJson(URL, paraMap);
				if(null!=result&&StringUtils.isNotBlank(result.getString("msg"))){
					return result.getString("msg").equals("success");
				}
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
