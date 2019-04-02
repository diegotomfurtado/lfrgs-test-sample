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
@Browsers({
	BrowserType.HTMLUNIT,
	BrowserType.CHROME,
	BrowserType.FIREFOX,
	BrowserType.IE
})
@RunWith(LiferaySeleniumTestRunner.class)
public class BrowserAnnotationRunnerTest extends BaseTest {

	@Test
	public void testBrowsers() throws Exception {
		testGoogle(webDriver);
	}

	@WebDriverField
	private WebDriver webDriver;

}