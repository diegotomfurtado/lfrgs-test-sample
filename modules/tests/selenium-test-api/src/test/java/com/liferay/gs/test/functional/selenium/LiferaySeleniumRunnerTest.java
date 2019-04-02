package com.liferay.gs.test.functional.selenium;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.liferay.gs.test.functional.selenium.runner.Browsers;
import com.liferay.gs.test.functional.selenium.runner.LiferaySeleniumTestRunner;
import com.liferay.gs.test.functional.selenium.runner.WebDriverField;

/**
 * @author Andrew Betts
 */
@Browsers
@RunWith(LiferaySeleniumTestRunner.class)
public class LiferaySeleniumRunnerTest extends BaseTest {

	@Test
	public void testGoogleSearch1() throws Exception {
		testGoogle(_webDriver);
	}

	@Test
	public void testGoogleSearch2() throws Exception {
		testGoogle(_webDriver);
	}

	@WebDriverField
	private WebDriver _webDriver;

}