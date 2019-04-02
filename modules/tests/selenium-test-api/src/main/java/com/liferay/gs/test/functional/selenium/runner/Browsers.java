package com.liferay.gs.test.functional.selenium.runner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.remote.BrowserType;

/**
 * @author Andrew Betts
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Browsers {

	public String[] value() default {BrowserType.HTMLUNIT};

	public boolean restartService() default false;

}