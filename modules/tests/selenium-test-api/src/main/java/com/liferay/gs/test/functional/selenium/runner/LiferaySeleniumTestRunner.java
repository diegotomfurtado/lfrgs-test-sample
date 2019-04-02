package com.liferay.gs.test.functional.selenium.runner;

import com.liferay.gs.test.functional.selenium.rule.WebDriverTestRule;
import com.liferay.gs.test.functional.selenium.support.WebDriverManager;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverManagerThreadLocal;
import com.liferay.gs.test.functional.selenium.threadlocal.WebDriverThreadLocal;

import java.util.List;

import org.junit.internal.runners.statements.InvokeMethod;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import org.openqa.selenium.WebDriver;

/**
 * @author Andrew Betts
 */
public class LiferaySeleniumTestRunner extends BlockJUnit4ClassRunner {

	public LiferaySeleniumTestRunner(Class<?> klass)
		throws InitializationError {

		super(klass);

		_webDriverManager = new WebDriverManager();

		_testClass = getTestClass();
	}

	@Override
	protected Statement classBlock(RunNotifier notifier) {
		List<TestRule> classRules = classRules();

		Statement statement = super.classBlock(notifier);

		for (TestRule classRule : classRules) {
			if (classRule instanceof WebDriverTestRule) {
				return statement;
			}
		}

		Browsers browsers = _testClass.getAnnotation(Browsers.class);

		if (browsers == null) {
			return statement;
		}

		String[] classLevelBrowsers = browsers.value();
		boolean restartService = true;

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				WebDriver threadLocalWebDriver = WebDriverThreadLocal.get();
				WebDriverManager threadLocalWebDriverManager =
					WebDriverManagerThreadLocal.get();

				for (String browser : classLevelBrowsers) {
					WebDriver webDriver = null;

					if (threadLocalWebDriverManager != null) {
						if (!threadLocalWebDriverManager.isAvailable(browser)) {
							notifier.fireTestIgnored(getDescription());

							continue;
						}

						webDriver = threadLocalWebDriverManager.getWebDriver(
							browser, restartService);
					}
					else {
						if (!_webDriverManager.isAvailable(browser)) {
							notifier.fireTestIgnored(getDescription());

							continue;
						}

						webDriver = _webDriverManager.getWebDriver(
							browser, restartService);
					}

					WebDriverThreadLocal.set(webDriver);

					statement.evaluate();

					webDriver.quit();

					WebDriverThreadLocal.set(null);
				}

				WebDriverThreadLocal.set(threadLocalWebDriver);
			}
		};
	}

	@Override
	protected Statement methodInvoker(FrameworkMethod method, Object test) {
		Statement statement = new InvokeMethod(method, test);

		List<TestRule> testRules = getTestRules(test);

		for (TestRule testRule : testRules) {
			if (testRule instanceof WebDriverTestRule) {
				return statement;
			}
		}

		List<FrameworkField> webDriverFields =
			_testClass.getAnnotatedFields(WebDriverField.class);

		if ((webDriverFields == null) || webDriverFields.isEmpty()) {
			return statement;
		}

		return new WebDriverFieldAwareStatement(
			statement, test, webDriverFields, _webDriverManager);
	}

	@Override
	protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		if (isIgnored(method)) {
			notifier.fireTestIgnored(description);

			return;
		}

		Statement statement = methodBlock(method);

		Browsers browsers = method.getAnnotation(Browsers.class);

		if (browsers == null) {
			runLeaf(statement, description, notifier);
		}
		else {
			WebDriver threadLocalWebDriver = WebDriverThreadLocal.get();
			WebDriverManager webDriverManager =
				WebDriverManagerThreadLocal.get();

			if (webDriverManager == null) {
				webDriverManager = _webDriverManager;
			}

			boolean restartService = browsers.restartService();

			for (String browser : browsers.value()) {
				if (!webDriverManager.isAvailable(browser)) {
					notifier.fireTestIgnored(description);

					continue;
				}

				WebDriver webDriver = null;

				if (threadLocalWebDriver == null) {
					webDriver = webDriverManager.getWebDriver(
						browser, restartService);

					WebDriverThreadLocal.set(webDriver);
				}

				runLeaf(statement, description, notifier);

				if (webDriver != null) {
					webDriver.quit();
				}

				WebDriverThreadLocal.set(threadLocalWebDriver);
			}
		}
	}

	private WebDriverManager _webDriverManager;
	private TestClass _testClass;

}