package com.liferay.gs.test.functional.selenium.rule;

import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;
import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverThreadLocal;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.runner.Description;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * @author Manoel Cyreno
 * @author Andrew Betts
 */
public class ScreenShotTestRule extends AbstractTestWatcher {

	public ScreenShotTestRule() {
		super(TestWatcherState.FAILED);
	}

	public ScreenShotTestRule(TestWatcherState[] when) {
		super(when);
	}

	public void takeScreenShot(WebDriver webDriver, String fileName) {
		File screenShot = null;

		if (webDriver instanceof TakesScreenshot) {
			TakesScreenshot takesScreenshot = (TakesScreenshot) webDriver;

			screenShot = takesScreenshot.getScreenshotAs(OutputType.FILE);
		}

		if (screenShot == null) {
			try {
				screenShot = _takeAWTScreenShot(fileName);
			}
			catch (AWTException | IOException e) {
				_log.error("unable to take screen shot", e);

				return;
			}
		}

		String screenShotParentDirPath = SeleniumProperties.get(
			SeleniumPropertyKeys.SCREEN_SHOT_DIR_PATH);

		File screenShotParentDir = new File(screenShotParentDirPath);

		try {
			FileUtils.copyFile(
				screenShot, new File(screenShotParentDir, fileName));
		}
		catch (IOException e) {
			_log.error(
				"unable to copy screen shot to destination" +
					screenShotParentDir.getPath(),
				e);
		}
	}

	/**
	 * Invoked when a test fails
	 */
	@Override
	protected void doFailed(Throwable e, Description description) {
		WebDriver webDriver = WebDriverThreadLocal.get();

		String fileName =
			description.getClassName() + "-" + description.getMethodName() +
				"-failure.png";

		takeScreenShot(webDriver, fileName);
	}

	/**
	 * Invoked when a test method finishes (whether passing or failing)
	 */
	@Override
	protected void doFinished(Description description) {
		WebDriver webDriver = WebDriverThreadLocal.get();

		String fileName =
			description.getClassName() + "-" + description.getMethodName() +
				"-finish.png";

		takeScreenShot(webDriver, fileName);
	}

	/**
	 * Invoked when a test is about to start
	 */
	@Override
	protected void doStarting(Description description) {
		WebDriver webDriver = WebDriverThreadLocal.get();

		String fileName =
			description.getClassName() + "-" + description.getMethodName() +
				"-start.png";

		takeScreenShot(webDriver, fileName);
	}

	/**
	 * Invoked when a test succeeds
	 */
	@Override
	protected void doSucceeded(Description description) {
		WebDriver webDriver = WebDriverThreadLocal.get();

		String fileName =
			description.getClassName() + "-" + description.getMethodName() +
				"-success.png";

		takeScreenShot(webDriver, fileName);
	}

	private File _takeAWTScreenShot(String fileName)
		throws AWTException, IOException {

		Robot robot = new Robot();

		Toolkit toolkit = Toolkit.getDefaultToolkit();

		Rectangle screen = new Rectangle(toolkit.getScreenSize());

		BufferedImage image = robot.createScreenCapture(screen);

		File tempDir = FileUtils.getTempDirectory();

		File screenShot = new File(tempDir, fileName);

		screenShot.deleteOnExit();

		ImageIO.write(image, "png", screenShot);

		return screenShot;
	}

	private static final Log _log = LogFactory.getLog(ScreenShotTestRule.class);

}