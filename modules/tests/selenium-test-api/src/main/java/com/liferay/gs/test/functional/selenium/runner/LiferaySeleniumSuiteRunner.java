package com.liferay.gs.test.functional.selenium.runner;

import com.liferay.gs.test.functional.selenium.support.WebDriverManager;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * @author Andrew Betts
 */
public class LiferaySeleniumSuiteRunner extends Suite {

	public LiferaySeleniumSuiteRunner(Class<?> klass, RunnerBuilder builder)
		throws InitializationError {

		super(klass, new BrowserRunnerBuilder(builder, new WebDriverManager()));
	}

}