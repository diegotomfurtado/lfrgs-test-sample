package com.liferay.gs.test.functional.selenium.threadlocal;

import com.liferay.gs.test.functional.selenium.support.WebDriverManager;

/**
 * @author Andrew Betts
 */
public class WebDriverManagerThreadLocal {

	private static final ThreadLocal<WebDriverManager> _threadLocal =
		new ThreadLocal<>();

	public static WebDriverManager get() {
		return _threadLocal.get();
	}

	public static void set(WebDriverManager webDriverManager) {
		_threadLocal.set(webDriverManager);
	}

}