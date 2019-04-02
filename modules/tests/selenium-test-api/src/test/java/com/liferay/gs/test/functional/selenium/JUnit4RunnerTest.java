package com.liferay.gs.test.functional.selenium;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.liferay.gs.test.functional.selenium.constants.BrowserDrivers;
import com.liferay.gs.test.functional.selenium.rule.WebDriverTestRule;

/**
 * @author Andrew Betts
 */
@RunWith(JUnit4.class)
public class JUnit4RunnerTest extends BaseTest {

	@ClassRule
	public static WebDriverTestRule webDriverClassRule =
		new WebDriverTestRule(new String[] {BrowserDrivers.BROWSER_CHROME});

	@Test
	public void testGoogleSearch1() throws Exception {
		testGoogle(webDriverClassRule.getWebDriver());
	}

	@Test
	public void testGoogleSearch2() throws Exception {
		testGoogle(webDriverClassRule.getWebDriver());
	}

}