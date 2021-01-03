package com.yonglilian.intercept.annotation;

import java.lang.annotation.*;

/**
 * 安全验证的注解类.
 * @author lwk
 *
 */
@Target(value = {ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZrSafety {
	
	String value() default "on";// "on"：打开[默认]，"off"：关闭
}
