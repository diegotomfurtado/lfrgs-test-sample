package com.liferay.gs.test.functional.selenium.suite;

import com.liferay.gs.test.functional.selenium.BaseTest;
import com.liferay.gs.test.functional.selenium.rule.WebDriverTestRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Andrew Betts
 */
public class JUnit4SuiteTest2 extends BaseTest {

	@Rule
	public WebDriverTestRule webDriverClassRule =
		new WebDriverTestRule();

	@Test
	public void testGoogleSearch() throws Exception {
		testGoogle(webDriverClassRule.getWebDriver());
	}

}