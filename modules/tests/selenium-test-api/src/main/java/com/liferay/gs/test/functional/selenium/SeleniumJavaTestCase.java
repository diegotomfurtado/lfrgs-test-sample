package com.liferay.gs.test.functional.selenium;

import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;
import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;
import com.liferay.gs.test.functional.selenium.support.WebDriverManager;

import junit.framework.TestCase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;

/**
 * @author Andrew Betts
 */
public abstract class SeleniumJavaTestCase extends TestCase {

	protected String getBrowser() {
		String defaultBrowser = SeleniumProperties.get(
			SeleniumPropertyKeys.TEST_DEFAULT_BROWSER);

		if (defaultBrowser == null) {
			defaultBrowser = BrowserType.HTMLUNIT;
		}

		return defaultBrowser;
	}

	@Override
	protected void setUp() throws Exception {
		webDriver = _WEB_DRIVER_MANAGER.getWebDriver(getBrowser());
	}

	@Override
	protected void tearDown() throws Exception {
		webDriver.quit();

		_WEB_DRIVER_MANAGER.stopService();

		webDriver = null;
	}

	private static WebDriverManager _WEB_DRIVER_MANAGER =
		new WebDriverManager();

	protected WebDriver webDriver;

}
