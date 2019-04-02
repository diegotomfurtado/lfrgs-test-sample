package com.liferay.gs.test.functional.selenium.runner;

import com.liferay.gs.test.functional.selenium.support.WebDriverManager;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverManagerThreadLocal;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverThreadLocal;

import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
public class BrowserRunner extends Suite {

	public BrowserRunner(
		String browser, boolean restartService,
		WebDriverManager webDriverManager, Class<?> runnerClass,
		List<Runner> runners)
		throws InitializationError {

		super(runnerClass, runners);

		_browser = browser;
		_restartService = restartService;
		_webDriverManager = webDriverManager;
	}

	@Override
	protected String getName() {
		return BrowserRunner.class.getName() + "#" + _browser;
	}

	@Override
	public void run(final RunNotifier notifier) {
		WebDriverManagerThreadLocal.set(_webDriverManager);

		if (!_webDriverManager.isAvailable(_browser)) {
			notifier.fireTestIgnored(getDescription());

			return;
		}

		WebDriver webDriver = _webDriverManager.getWebDriver(
			_browser, _restartService);

		WebDriverThreadLocal.set(webDriver);

		super.run(notifier);

		webDriver.quit();

		WebDriverThreadLocal.set(null);
		WebDriverManagerThreadLocal.set(null);
	}

	private String _browser;
	private boolean _restartService;
	private WebDriverManager _webDriverManager;

}
