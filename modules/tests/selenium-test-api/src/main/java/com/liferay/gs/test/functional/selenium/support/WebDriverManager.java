package com.liferay.gs.test.functional.selenium.support;

import com.liferay.gs.test.functional.selenium.constants.BrowserDrivers;
import com.liferay.gs.test.functional.selenium.constants.Environment;
import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;
import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

/**
 * @author Andrew Betts
 */
public class WebDriverManager {

	@Override
	public void finalize() throws Throwable {
		stopService();

		super.finalize();
	}

	public WebDriver getWebDriver() {
		String defaultBrowser = SeleniumProperties.get(
			SeleniumPropertyKeys.TEST_DEFAULT_BROWSER);

		if (defaultBrowser == null) {
			defaultBrowser = BrowserType.HTMLUNIT;
		}

		return getWebDriver(defaultBrowser, false);
	}

	public WebDriver getWebDriver(String browserType) {
		return getWebDriver(browserType, false);
	}

	public WebDriver getWebDriver(
		String browserType, boolean stopActiveService) {

		if (_availableWebDrivers.isEmpty()) {
			throw new IllegalStateException("no web drivers are available");
		}

		if (!_availableWebDrivers.contains(browserType)) {
			throw new IllegalArgumentException(
				"no web driver available for " + browserType);
		}

		if (_webDriver != null) {
			_webDriver.quit();
		}

		if (stopActiveService && (_activeService != null)) {
			_activeService.stop();
		}

		DriverService driverService = null;
		Capabilities capabilities = null;

		switch (browserType) {
			case BrowserType.CHROME:
			case BrowserType.GOOGLECHROME:
				if (_chromeDriverService == null) {
					_chromeDriverService =
						ChromeDriverService.createDefaultService();
				}

				driverService = _chromeDriverService;

				capabilities = BrowserOptionsParser.getCapabilities(
					SeleniumPropertyKeys.BROWSER_OPTIONS_CHROME_PREFIX,
					new ChromeOptions());

				break;

			case BrowserType.FIREFOX:
			case BrowserType.FIREFOX_PROXY:
				if (_geckoDriverService == null) {
					_geckoDriverService =
						GeckoDriverService.createDefaultService();
				}

				driverService = _geckoDriverService;

				capabilities = BrowserOptionsParser.getCapabilities(
					SeleniumPropertyKeys.BROWSER_OPTIONS_GECKO_PREFIX,
					new FirefoxOptions());

				break;

			case BrowserType.IE:
			case BrowserType.IE_HTA:
			case BrowserType.IEXPLORE:
			case BrowserType.IEXPLORE_PROXY:
				if (_ieDriverService == null) {
					_ieDriverService =
						InternetExplorerDriverService.createDefaultService();
				}

				driverService = _ieDriverService;

				capabilities = BrowserOptionsParser.getCapabilities(
					SeleniumPropertyKeys.BROWSER_OPTIONS_IE_PREFIX,
					new InternetExplorerOptions());

				break;

			default:
				if (_webDriver != null) {
					_webDriver.quit();

					_webDriver = null;
				}

				if (_activeService != null) {
					_activeService.stop();

					_activeService = null;
				}

				Capabilities htmlUnitCapabilities =
					BrowserOptionsParser.getCapabilities(
						SeleniumPropertyKeys.BROWSER_OPTIONS_UNIT_HTML_PREFIX,
						DesiredCapabilities.htmlUnit());

				return new HtmlUnitDriver(htmlUnitCapabilities);
		}

		if (!driverService.equals(_activeService)) {
			if (_activeService != null) {
				_activeService.stop();
			}

			_activeService = driverService;
		}

		try {
			driverService.start();
		}
		catch (IOException ioe) {
			throw new UncheckedIOException(ioe);
		}

		_webDriver = new RemoteWebDriver(driverService.getUrl(), capabilities);

		return _webDriver;
	}

	public boolean isAvailable(String browser) {
		return _availableWebDrivers.contains(browser);
	}

	public void stopService() {
		if (_webDriver != null) {
			_webDriver.quit();
		}

		if (_activeService != null) {
			_activeService.stop();
		}
	}

	private static void _init() {
		boolean is64Bit = Environment.is64Bit();

		ClassLoader classLoader = WebDriverManager.class.getClassLoader();

		String driverPath = SeleniumProperties.get(
			SeleniumPropertyKeys.SELENIUM_DRIVERS_DIR_PATH);

		if ((driverPath == null) || driverPath.isEmpty()) {
			try {
				URL url = classLoader.getResource("drivers");

				if (url != null) {
					File driverFolder = new File(url.toURI());

					driverPath = driverFolder.getPath();
				}
			}
			catch (Exception e) {
			}
		}

		if (driverPath == null) {
			throw new UncheckedIOException(
				new IOException("unable to load web drivers"));
		}

		if (!driverPath.endsWith("/")) {
			driverPath += "/";
		}

		_availableWebDrivers.clear();

		_availableWebDrivers.add(BrowserType.HTMLUNIT);

		switch (Platform.getCurrent()) {
			case LINUX:
				if (is64Bit) {
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_CHROME_DRIVER,
						driverPath + BrowserDrivers.CHROME_DRIVER_LINUX64);
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_GECKO_DRIVER,
						driverPath + BrowserDrivers.FIREFOX_DRIVER_LINUX32);

					_availableWebDrivers.add(BrowserDrivers.BROWSER_CHROME);
					_availableWebDrivers.add(BrowserType.FIREFOX);
				}
				else {
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_GECKO_DRIVER,
						driverPath + BrowserDrivers.FIREFOX_DRIVER_LINUX32);

					_availableWebDrivers.add(BrowserType.FIREFOX);
				}

				break;

			case MAC:
				if (is64Bit) {
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_CHROME_DRIVER,
						driverPath + BrowserDrivers.CHROME_DRIVER_MAC64);
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_GECKO_DRIVER,
						driverPath + BrowserDrivers.FIREFOX_DRIVER_MAC);

					_availableWebDrivers.add(BrowserDrivers.BROWSER_CHROME);
					_availableWebDrivers.add(BrowserType.FIREFOX);
				}
				else {
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_GECKO_DRIVER,
						driverPath + BrowserDrivers.FIREFOX_DRIVER_MAC);

					_availableWebDrivers.add(BrowserType.FIREFOX);
				}

				break;

			case WINDOWS:
				if (is64Bit) {
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_CHROME_DRIVER,
						driverPath + BrowserDrivers.CHROME_DRIVER_WIN32);
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_GECKO_DRIVER,
						driverPath + BrowserDrivers.FIREFOX_DRIVER_WIN32);
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_IE_DRIVER,
						driverPath + BrowserDrivers.INTERNET_EXPLORER_WIN64);

					_availableWebDrivers.add(BrowserType.CHROME);
					_availableWebDrivers.add(BrowserType.FIREFOX);
					_availableWebDrivers.add(
						BrowserType.IE);
				}
				else {
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_CHROME_DRIVER,
						driverPath + BrowserDrivers.CHROME_DRIVER_WIN32);
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_GECKO_DRIVER,
						driverPath + BrowserDrivers.FIREFOX_DRIVER_WIN32);
					System.setProperty(
						SeleniumPropertyKeys.WEBDRIVER_IE_DRIVER,
						driverPath + BrowserDrivers.INTERNET_EXPLORER_WIN32);

					_availableWebDrivers.add(BrowserType.CHROME);
					_availableWebDrivers.add(BrowserType.FIREFOX);
					_availableWebDrivers.add(
						BrowserType.IE);
				}
		}
	}

	private static final List<String> _availableWebDrivers = new ArrayList<>();

	static {
		_init();
	}

	private DriverService _activeService;
	private WebDriver _webDriver;

	private ChromeDriverService _chromeDriverService;
	private GeckoDriverService _geckoDriverService;
	private InternetExplorerDriverService _ieDriverService;

}
