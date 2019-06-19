package com.bj58.daojia.qa.annotation;

import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target; 
/**
 * dsf接口注解
 * @author huaiq
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.TYPE,ElementType.FIELD})  
public @interface DSF {
	String url();//dsf请求url
}
