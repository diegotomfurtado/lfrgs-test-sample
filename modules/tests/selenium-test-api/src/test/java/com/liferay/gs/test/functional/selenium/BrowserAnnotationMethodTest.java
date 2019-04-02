package com.liferay.gs.test.functional.selenium;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;

import com.liferay.gs.test.functional.selenium.runner.Browsers;
import com.liferay.gs.test.functional.selenium.runner.LiferaySeleniumTestRunner;
import com.liferay.gs.test.functional.selenium.runner.WebDriverField;

/**
 * @author Andrew Betts
 */
@RunWith(LiferaySeleniumTestRunner.class)
public class BrowserAnnotationMethodTest extends BaseTest {

	@Test
	@Browsers({
		BrowserType.HTMLUNIT,
		BrowserType.CHROME,
		BrowserType.FIREFOX,
		BrowserType.IE
	})
	public void testBrowsers() throws Exception {
		testGoogle(webDriver);
	}

	@WebDriverField
	private WebDriver webDriver;

}