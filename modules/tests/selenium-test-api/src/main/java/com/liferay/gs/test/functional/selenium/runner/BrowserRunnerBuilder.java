package com.liferay.gs.test.functional.selenium.runner;

import com.liferay.gs.test.functional.selenium.support.WebDriverManager;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * @author Andrew Betts
 */
public class BrowserRunnerBuilder extends RunnerBuilder {

	public BrowserRunnerBuilder(
		RunnerBuilder builder, WebDriverManager webDriverManager) {

		_builder = builder;
		_webDriverManager = webDriverManager;
	}

	public List<Runner> runners(Class<?> parent, Class<?>[] children)
		throws InitializationError {

		List<Runner> runners = super.runners(parent, children);

		List<Runner> browserRunners = new ArrayList<>();

		Browsers browsers = parent.getAnnotation(Browsers.class);

		if ((browsers == null) || (browsers.value().length == 0)) {
			return runners;
		}

		for (String browser : browsers.value()) {
			browserRunners.add(
				getBrowserRunner(
					browser, browsers.restartService(), _webDriverManager,
					parent, runners));
		}

		return browserRunners;
	}

	protected Runner getBrowserRunner(
			String browser, boolean restartService,
			WebDriverManager webDriverManager, Class<?> runnerClass,
			List<Runner> runners)
		throws InitializationError {

		return new BrowserRunner(
			browser, restartService, webDriverManager, runnerClass, runners);
	}

	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {
		return _builder.runnerForClass(testClass);
	}

	private RunnerBuilder _builder;
	private WebDriverManager _webDriverManager;
}
