package com.liferay.gs.test.functional.selenium.rule;

import com.liferay.gs.test.functional.selenium.BaseTest;

import com.liferay.gs.test.functional.selenium.constants.BrowserDrivers;
import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;
import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;
import com.liferay.gs.test.functional.selenium.support.WebDriverManager;

import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.model.Statement;

import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
@RunWith(JUnit4.class)
public class ScreenShotTestRuleTest extends BaseTest {

	public AssertScreenShotTestWatcher assertScreenShotTestWatcher =
		new AssertScreenShotTestWatcher();

	public ScreenShotTestRule screenShotTestRule = new ScreenShotTestRule();

	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Rule
	public SimpleRuleChain simpleRuleChain = new SimpleRuleChain(
		screenShotTestRule, assertScreenShotTestWatcher, temporaryFolder);

	@Test
	public void testScreenShotNoWebDriver() throws Exception {
		File folder = temporaryFolder.newFolder();

		SeleniumProperties.set(
			SeleniumPropertyKeys.SCREEN_SHOT_DIR_PATH, folder.getPath());

		screenShotTestRule.takeScreenShot(null, "screenShot1");

		File[] files = folder.listFiles();

		Assert.assertNotNull(files);
		Assert.assertEquals(files.length, 1);
		Assert.assertEquals("screenShot1", files[0].getName());
	}

	@Test
	public void testScreenShotWebDriver() throws Exception {
		File folder = temporaryFolder.newFolder();

		SeleniumProperties.set(
			SeleniumPropertyKeys.SCREEN_SHOT_DIR_PATH, folder.getPath());

		WebDriverManager webDriverManager = new WebDriverManager();

		WebDriver webDriver =
			webDriverManager.getWebDriver(BrowserDrivers.BROWSER_CHROME);

		testGoogle(webDriver);

		screenShotTestRule.takeScreenShot(webDriver, "screenShot2");

		webDriverManager.stopService();

		File[] files = folder.listFiles();

		Assert.assertNotNull(files);
		Assert.assertEquals(files.length, 1);
		Assert.assertEquals("screenShot2", files[0].getName());
	}

	@Test
	public void testScreenShotOnFail() throws Exception {
		File folder = temporaryFolder.newFolder();

		SeleniumProperties.set(
			SeleniumPropertyKeys.SCREEN_SHOT_DIR_PATH, folder.getPath());

		assertScreenShotTestWatcher.expectFail = true;

		Assert.fail();
	}

	private class AssertScreenShotTestWatcher implements TestRule {

		@Override
		public Statement apply(Statement base, Description description) {
			return new Statement() {

				@Override
				public void evaluate() throws Throwable {
					try {
						base.evaluate();
					}
					catch (AssertionError ae) {
						if (expectFail) {
							String screenShotDirPath = SeleniumProperties.get(
								SeleniumPropertyKeys.SCREEN_SHOT_DIR_PATH);

							File folder = new File(screenShotDirPath);

							File[] files = folder.listFiles();

							Assert.assertNotNull(files);
							Assert.assertEquals(files.length, 1);
						}
						else {
							throw ae;
						}
					}
					finally {
						expectFail = false;
					}
				}

			};
		}

		private boolean expectFail;

	}

}