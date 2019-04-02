package com.liferay.gs.test.functional.selenium.rule;

import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;
import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;
import com.liferay.gs.test.functional.selenium.support.WebDriverManager;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverManagerThreadLocal;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverThreadLocal;

import java.util.Optional;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;


/**
 * @author Andrew Betts
 */
public class WebDriverTestRule implements TestRule {

	public WebDriverTestRule() {
		this(
			new String[] {
				Optional.ofNullable(
					SeleniumProperties.get(
						SeleniumPropertyKeys.TEST_DEFAULT_BROWSER)).orElse(
							BrowserType.HTMLUNIT)},
			false);
	}

	public WebDriverTestRule(String[] browsers) {
		this(browsers, false);
	}

	public WebDriverTestRule(String[] browsers, boolean restartService) {
		_browsers = browsers;
		_restartService = restartService;

		WebDriverManager webDriverManager = WebDriverManagerThreadLocal.get();

		if (webDriverManager == null) {
			WebDriverManagerThreadLocal.set(new WebDriverManager());
		}
	}

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				WebDriverManager webDriverManager =
					WebDriverManagerThreadLocal.get();

				for (String browser : _browsers) {
					WebDriver webDriver = webDriverManager.getWebDriver(
						browser, _restartService);

					WebDriverThreadLocal.set(webDriver);

					base.evaluate();

					webDriver.quit();

					WebDriverThreadLocal.set(null);
				}
			}

		};
	}

	public WebDriver getWebDriver() {
		return WebDriverThreadLocal.get();
	}

	private String[] _browsers;
	private boolean _restartService;

}