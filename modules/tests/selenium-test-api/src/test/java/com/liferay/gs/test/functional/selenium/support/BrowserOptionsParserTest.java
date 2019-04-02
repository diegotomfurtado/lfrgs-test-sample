package com.liferay.gs.test.functional.selenium.support;

import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;
import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.internal.ElementScrollBehavior;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Andrew Betts
 */
@RunWith(JUnit4.class)
public class BrowserOptionsParserTest {

	@AfterClass
	public static void tearDownClass() throws Exception {
		SeleniumProperties.loadProperties(null);
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		ClassLoader classLoader = BrowserOptionsParser.class.getClassLoader();

		URL url = classLoader.getResource("test.selenium.properties");

		if (url == null) {
			throw new IllegalStateException(
				"unable to locate test.selenium.properties file");
		}

		File file = new File(url.toURI());

		SeleniumProperties.loadProperties(file);
	}

	@Test
	public void testChromeOptions() {
		ChromeOptions chromeOptions = new ChromeOptions();

		BrowserOptionsParser.getCapabilities(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CHROME_PREFIX, chromeOptions);

		Map<String, Object> options = chromeOptions.asMap();

		Map<String, Object> chromeOptionsMap =
			(Map<String, Object>)options.get("goog:chromeOptions");

		List<String> args = (List<String>)chromeOptionsMap.get("args");

		Assert.assertEquals(3, args.size());
		Assert.assertEquals("arg1", args.get(0));

		Assert.assertEquals("world", chromeOptionsMap.get("hello"));
	}

	@Test
	public void testFirefoxOptions() {
		FirefoxOptions firefoxOptions = new FirefoxOptions();

		BrowserOptionsParser.getCapabilities(
			SeleniumPropertyKeys.BROWSER_OPTIONS_GECKO_PREFIX, firefoxOptions);

		Map<String, Object> options = firefoxOptions.asMap();

		Assert.assertTrue(firefoxOptions.isLegacy());

		Map<String, Object> firefoxOptionsMap =
			(Map<String, Object>)options.get("moz:firefoxOptions");

		List<String> args = (List<String>)firefoxOptionsMap.get("args");

		Assert.assertEquals(3, args.size());
		Assert.assertEquals("arg1", args.get(0));

		Map<String, Object> logPrefs =
			(Map<String, Object>)firefoxOptionsMap.get("log");
		FirefoxDriverLogLevel logLevel =
			(FirefoxDriverLogLevel)logPrefs.get("level");

		Assert.assertEquals(FirefoxDriverLogLevel.DEBUG, logLevel);

		Map<String, Object> prefs =
			(Map<String, Object>)firefoxOptionsMap.get("prefs");

		Assert.assertEquals(3, prefs.size());
		Assert.assertEquals(true, prefs.get("pref1"));
		Assert.assertEquals(42, prefs.get("pref2"));
		Assert.assertEquals("world", prefs.get("pref3"));
	}

	@Test
	public void testGeneralOptions() {
		ChromeOptions chromeOptions = new ChromeOptions();

		BrowserOptionsParser.getCapabilities(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CHROME_PREFIX, chromeOptions);

		Map<String, Object> options = chromeOptions.asMap();

		Assert.assertEquals(
			true, options.get(CapabilityType.ACCEPT_INSECURE_CERTS));

		Assert.assertEquals(
			PageLoadStrategy.NORMAL,
			options.get(CapabilityType.PAGE_LOAD_STRATEGY));

		Assert.assertEquals(
			UnexpectedAlertBehaviour.ACCEPT_AND_NOTIFY,
			options.get(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR));

		Assert.assertEquals("world", options.get("hello"));
	}

	@Test
	public void testHtmlUnitOptions() {
		DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

		BrowserOptionsParser.getCapabilities(
			SeleniumPropertyKeys.BROWSER_OPTIONS_UNIT_HTML_PREFIX,
			desiredCapabilities);

		Assert.assertEquals(Platform.ANY, desiredCapabilities.getPlatform());
		Assert.assertEquals("chrome", desiredCapabilities.getBrowserName());
		Assert.assertEquals("11", desiredCapabilities.getVersion());
		Assert.assertTrue(
			desiredCapabilities.is(CapabilityType.SUPPORTS_JAVASCRIPT));
	}

	@Test
	public void testIEOptions() {
		InternetExplorerOptions internetExplorerOptions =
			new InternetExplorerOptions();

		BrowserOptionsParser.getCapabilities(
			SeleniumPropertyKeys.BROWSER_OPTIONS_IE_PREFIX,
			internetExplorerOptions);

		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.NATIVE_EVENTS));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.IGNORE_ZOOM_SETTING));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.
					INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.REQUIRE_WINDOW_FOCUS));
		Assert.assertTrue(
			internetExplorerOptions.is("ie.enableFullPageScreenshot"));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.FORCE_CREATE_PROCESS));
		Assert.assertTrue(
			internetExplorerOptions.is(
				InternetExplorerDriver.IE_USE_PER_PROCESS_PROXY));
		Assert.assertTrue(
			internetExplorerOptions.is("ie.forceShellWindowsApi"));

		Map<String, Object> options = internetExplorerOptions.asMap();

		Assert.assertEquals(
			ElementScrollBehavior.TOP.getValue(),
			options.get(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR));
		Assert.assertEquals(
			1000L, options.get("ie.fileUploadDialogTimeout"));
		Assert.assertEquals(
			1000L, options.get(InternetExplorerDriver.BROWSER_ATTACH_TIMEOUT));

		List<String> switches =
			(List<String>)options.get("ie.browserCommandLineSwitches");

		Assert.assertEquals(2, switches.size());
		Assert.assertEquals("hello", switches.get(0));
	}

	@Test
	public void testProxyOptions() {
		ChromeOptions chromeOptions = new ChromeOptions();

		BrowserOptionsParser.getCapabilities(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CHROME_PREFIX, chromeOptions);

		Map<String, Object> options = chromeOptions.asMap();

		Object proxyObject = options.get("proxy");

		Assert.assertTrue(proxyObject instanceof Proxy);

		Proxy proxy = (Proxy)proxyObject;

		Assert.assertEquals(Proxy.ProxyType.MANUAL, proxy.getProxyType());

		Assert.assertEquals("ftpProxy", proxy.getFtpProxy());
		Assert.assertEquals("httpProxy", proxy.getHttpProxy());
		Assert.assertEquals("noProxy", proxy.getNoProxy());
		Assert.assertEquals("socksPassword", proxy.getSocksPassword());
		Assert.assertEquals("socksProxy", proxy.getSocksProxy());
		Assert.assertEquals("socksUserName", proxy.getSocksUsername());
		Assert.assertEquals(new Integer(1), proxy.getSocksVersion());
		Assert.assertEquals("sslProxy", proxy.getSslProxy());
	}

}