package com.liferay.gs.test.functional.selenium.suite;

import com.liferay.gs.test.functional.selenium.BaseTest;
import com.liferay.gs.test.functional.selenium.runner.LiferaySeleniumTestRunner;
import com.liferay.gs.test.functional.selenium.runner.WebDriverField;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
@RunWith(LiferaySeleniumTestRunner.class)
public class SeleniumSuiteTest1 extends BaseTest {

	@Test
	public void testGoogleSearch() throws Exception {
		testGoogle(webDriver);
	}

	@WebDriverField
	private WebDriver webDriver;

}