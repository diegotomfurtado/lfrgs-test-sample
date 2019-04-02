package com.liferay.gs.test.functional.selenium.runner;

import com.liferay.gs.test.functional.selenium.support.WebDriverManager;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverManagerThreadLocal;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverThreadLocal;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.Statement;

import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
class WebDriverFieldAwareStatement extends Statement {

	public WebDriverFieldAwareStatement(
		Statement statement, Object test,
		List<FrameworkField> webDriverFields,
		WebDriverManager webDriverManager) {

		_statement = statement;
		_test = test;
		_webDriverFields = webDriverFields;
		_webDriverManager = webDriverManager;
	}

	@Override
	public void evaluate() throws Throwable {
		WebDriver threadLocalWebDriver = WebDriverThreadLocal.get();
		WebDriverManager threadLocalWebDriverManager =
			WebDriverManagerThreadLocal.get();

		WebDriver webDriver = null;

		if (threadLocalWebDriver == null) {
			if (threadLocalWebDriverManager != null) {
				webDriver = threadLocalWebDriverManager.getWebDriver();
			} else {
				webDriver = _webDriverManager.getWebDriver();
			}

			_setFields(_webDriverFields, _test, webDriver);
		} else {
			_setFields(_webDriverFields, _test, threadLocalWebDriver);
		}

		_statement.evaluate();

		if (webDriver != null) {
			webDriver.quit();
		}

		_setFields(_webDriverFields, _test, null);
	}

	private void _setFields(
		List<FrameworkField> frameworkFields, Object test, Object value) {

		for (FrameworkField frameworkField : frameworkFields) {
			Field field = frameworkField.getField();

			field.setAccessible(true);

			try {
				field.set(test, value);
			} catch (IllegalAccessException e) {
			}
		}
	}

	private Statement _statement;
	private Object _test;
	private List<FrameworkField> _webDriverFields;
	private WebDriverManager _webDriverManager;

}