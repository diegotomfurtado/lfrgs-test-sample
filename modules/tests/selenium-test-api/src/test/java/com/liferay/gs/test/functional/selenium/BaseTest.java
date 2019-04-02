package com.liferay.gs.test.functional.selenium;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
public class BaseTest {

	protected void testGoogle(WebDriver webDriver) throws Exception {
		webDriver.navigate().to("https://www.google.com");

		Assert.assertEquals("Google", webDriver.getTitle());
	}

}
