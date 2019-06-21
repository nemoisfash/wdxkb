package cn.hxz.webapp.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * 用于通过反射机制获取实体属性的注释
 * @author lijing
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldMeta {
	/*
	 * 字段注解
	 */
	String name() default "";
	
	/*
	 * 定义字段是否可为空
	 */
	boolean notEmpty() default true;
}
