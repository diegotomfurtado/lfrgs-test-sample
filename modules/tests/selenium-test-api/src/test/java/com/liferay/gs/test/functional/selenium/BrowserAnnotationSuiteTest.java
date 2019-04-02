package com.liferay.gs.test.functional.selenium;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;

import com.liferay.gs.test.functional.selenium.runner.Browsers;
import com.liferay.gs.test.functional.selenium.runner.LiferaySeleniumSuiteRunner;
import com.liferay.gs.test.functional.selenium.runner.WebDriverField;
import com.liferay.gs.test.functional.selenium.suite.SeleniumSuiteTest1;

/**
 * @author Andrew Betts
 */
@Browsers({
	BrowserType.HTMLUNIT,
	BrowserType.CHROME,
	BrowserType.FIREFOX,
	BrowserType.IE
})
@RunWith(LiferaySeleniumSuiteRunner.class)
@Suite.SuiteClasses(SeleniumSuiteTest1.class)
public class BrowserAnnotationSuiteTest extends BaseTest {

	@Test
	public void testBrowsers()throws Exception {
		testGoogle(webDriver);
	}

	@WebDriverField
	private WebDriver webDriver;

}