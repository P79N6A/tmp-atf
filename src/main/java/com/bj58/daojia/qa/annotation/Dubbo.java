package com.bj58.daojia.qa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**   
 * @Description: TODO
 * @author xh   
 * @date 2017年3月3日 上午10:18:24   
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.FIELD})
public @interface Dubbo {
	String value() default "";
}
