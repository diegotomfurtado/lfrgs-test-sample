package com.liferay.gs.test.functional.selenium.threadlocal;

import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
public class WebDriverThreadLocal {

	private static final ThreadLocal<WebDriver> _threadLocal =
		new ThreadLocal<>();

	public static WebDriver get() {
		return _threadLocal.get();
	}

	public static void set(WebDriver webDriver) {
		_threadLocal.set(webDriver);
	}

}