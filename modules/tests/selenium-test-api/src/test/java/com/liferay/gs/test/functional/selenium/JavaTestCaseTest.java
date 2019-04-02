package com.liferay.gs.test.functional.selenium;

import org.junit.Assert;
import org.openqa.selenium.remote.BrowserType;

/**
 * @author Andrew Betts
 */
public class JavaTestCaseTest extends SeleniumJavaTestCase {

	public void testGoogleSearch() throws Exception {
		webDriver.navigate().to("https://www.google.com");

		Assert.assertEquals("Google", webDriver.getTitle());
	}

	protected String getBrowser() {
		return BrowserType.CHROME;
	}

}