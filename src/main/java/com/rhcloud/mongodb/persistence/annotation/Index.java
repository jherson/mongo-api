package com.rhcloud.mongodb.persistence.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Index {

	String key();
	boolean acsending() default true;
	boolean background() default false;
	boolean unique() default false;
	String name() default "";
	boolean dropDups() default false;
	int expireAfterSeconds() default -1;
	boolean sparse() default false;
	String defaultLanguage() default "";
	String languageOverride() default "";
}