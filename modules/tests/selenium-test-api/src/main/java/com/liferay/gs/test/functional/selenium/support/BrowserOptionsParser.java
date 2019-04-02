package com.liferay.gs.test.functional.selenium.support;

import com.liferay.gs.test.functional.selenium.properties.SeleniumProperties;
import com.liferay.gs.test.functional.selenium.properties.SeleniumPropertyKeys;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.internal.ElementScrollBehavior;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Andrew Betts
 *
 * list of supported properties
 *
 * browser prefixes:
 *
 * browser.options.chrome.
 * browser.options.gecko.
 * browser.options.ie.
 * browser.options.unit.html.
 *
 * generic properties (chrome, firefox, and ie):
 *
 * [browser.prefix.]acceptInsecureCerts=[true|false]
 * [browser.prefix.]pageLoadStrategy=[NONE|EAGER|NORMAL]
 * [browser.prefix.]unexpectedAlertBehaviour=[ACCEPT|DISMISS|ACCEPT_AND_NOTIFY|DISMISS_AND_NOTIFY|IGNORE]
 * [browser.prefix.]capabilities.[KEY]=[VALUE]
 * [browser.prefix.]proxy.enabled=[true|false]
 * [browser.prefix.]proxy.auto.detect=[true|false]
 * [browser.prefix.]proxy.ftp=[VALUE]
 * [browser.prefix.]proxy.http=[VALUE]
 * [browser.prefix.]proxy.no=[VALUE]
 * [browser.prefix.]proxy.autoconfig.url=[VALUE]
 * [browser.prefix.]proxy.socks=[VALUE]
 * [browser.prefix.]proxy.socks.password=[VALUE]
 * [browser.prefix.]proxy.socks.username=[VALUE]
 * [browser.prefix.]proxy.socks.version=[VALUE]
 * [browser.prefix.]proxy.ssl=[VALUE]
 * [browser.prefix.]proxy.type=[DIRECT|MANUAL|PAC|RESERVED_1|AUTODETECT|SYSTEM|UNSPECIFIED]
 *
 * chrome properties:
 *
 * browser.options.chrome.arguments=arg1,arg2,arg3
 * browser.options.chrome.extensions=path/to/ext1,path/to/ext2,path/to/ext3
 * browser.options.chrome.experimental.[KEY]=[VALUE]
 *
 * firefox properties:
 *
 * browser.options.gecko.arguments=arg1,arg2,arg3
 * browser.options.gecko.legacy=[true|false]
 * browser.options.gecko.log.level=[TRACE,DEBUG,CONFIG,INFO,WARN,ERROR,FATAL]
 * browser.options.gecko.preferences.boolean.[KEY]=[true|false]
 * browser.options.gecko.preferences.int.[KEY]=[INTEGER VALUE]
 * browser.options.gecko.preferences.string.[KEY]=[VALUE]
 * browser.options.gecko.profile.[KEY]=[VALUE] (todo)
 *
 * ie properties (some are double namespaced with 'ie'):
 *
 * browser.options.ie.ie.ensureCleanSession=[true|false]
 * browser.options.ie.nativeEvents=[true|false]
 * browser.options.ie.enablePersistentHover=[true|false]
 * browser.options.ie.ignoreZoomSetting=[true|false]
 * browser.options.ie.ignoreProtectedModeSettings=[true|false]
 * browser.options.ie.requireWindowFocus=[true|false]
 * browser.options.ie.ie.enableFullPageScreenshot=[true|false]
 * browser.options.ie.ie.forceCreateProcessApi=[true|false]
 * browser.options.ie.ie.usePerProcessProxy=[true|false]
 * browser.options.ie.ie.forceShellWindowsApi=[true|false]
 * browser.options.ie.elementScrollBehavior=[TOP|BOTTOM]
 * browser.options.ie.ie.fileUploadDialogTimeout=[LONG VALUE IN MILLIS]
 * browser.options.ie.browserAttachTimeout=[LONG VALUE IN MILLIS]
 * browser.options.ie.switches=switch1,switch2,switch3
 * browser.options.ie.initialBrowserUrl=[VALUE]
 *
 * unit html properties (using desired capabilities):
 *
 * browser.options.unit.html.javascriptEnabled=[true|false]
 * browser.options.unit.html.platformName=[WINDOWS|XP|VISTA|WIN8|WIN8_1|WIN10|MAC|SNOW_LEOPARD|MOUNTAIN_LION|MAVERICKS|YOSEMITE|EL_CAPITAN|SIERRA|UNIX|LINUX|ANDROID|IOS|ANY]
 * browser.options.unit.html.browserName=[VALUE]
 * browser.options.unit.html.browserVersion=[VALUE]
 *
 */
public class BrowserOptionsParser {

	public static Capabilities getCapabilities(
		String propertyPrefix, ChromeOptions chromeOptions) {

		Map<String, String> properties =
			SeleniumProperties.getByPrefix(propertyPrefix);

		_setArguments(
			SeleniumPropertyKeys.BROWSER_OPTIONS_ARGUMENTS, properties,
			chromeOptions::addArguments);

		_setBooleanOption(
			CapabilityType.ACCEPT_INSECURE_CERTS, properties,
			chromeOptions::setAcceptInsecureCerts);

		_setEnumOption(
			CapabilityType.PAGE_LOAD_STRATEGY, properties,
			chromeOptions::setPageLoadStrategy, PageLoadStrategy::valueOf);
		_setEnumOption(
			CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
			properties, chromeOptions::setUnhandledPromptBehaviour,
			UnexpectedAlertBehaviour::valueOf);

		_setFileOptions(properties, chromeOptions::addExtensions);

		_setKeyValueStringOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CAPABILITY_PREFIX, properties,
			chromeOptions::setCapability);
		_setKeyValueStringOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_EXPERIMENTAL_PREFIX,
			properties, chromeOptions::setExperimentalOption);

		_setProxy(properties, chromeOptions::setProxy);

		return chromeOptions;
	}

	public static Capabilities getCapabilities(
		String propertyPrefix, FirefoxOptions firefoxOptions) {

		Map<String, String> properties =
			SeleniumProperties.getByPrefix(propertyPrefix);

		_setArguments(
			SeleniumPropertyKeys.BROWSER_OPTIONS_ARGUMENTS, properties,
			firefoxOptions::addArguments);

		_setBooleanOption(
			CapabilityType.ACCEPT_INSECURE_CERTS, properties,
			firefoxOptions::setAcceptInsecureCerts);
		_setBooleanOption("legacy", properties, firefoxOptions::setLegacy);

		_setEnumOption(
			CapabilityType.PAGE_LOAD_STRATEGY,
			properties, firefoxOptions::setPageLoadStrategy, PageLoadStrategy::valueOf);
		_setEnumOption(
			CapabilityType.UNHANDLED_PROMPT_BEHAVIOUR,
			properties, firefoxOptions::setUnhandledPromptBehaviour,
			UnexpectedAlertBehaviour::valueOf);
		_setEnumOption(
			"log.level", properties, firefoxOptions::setLogLevel,
			FirefoxDriverLogLevel::valueOf);

		_setKeyValueStringOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CAPABILITY_PREFIX, properties,
			firefoxOptions::setCapability);

		_setKeyValueBooleanOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_PREFS_BOOLEAN_PREFIX,
			properties, firefoxOptions::addPreference);
		_setKeyValueIntOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_PREFS_INT_PREFIX,
			properties, firefoxOptions::addPreference);
		_setKeyValueStringOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_PREFS_STRING_PREFIX,
			properties, firefoxOptions::addPreference);

		_setProfile(properties, firefoxOptions::setProfile);

		_setProxy(properties, firefoxOptions::setProxy);

		return firefoxOptions;
	}

	public static Capabilities getCapabilities(
		String propertyPrefix, InternetExplorerOptions ieOptions) {

		Map<String, String> properties =
			SeleniumProperties.getByPrefix(propertyPrefix);

		_setArguments(
			SeleniumPropertyKeys.BROWSER_OPTIONS_IE_SWITCHES, properties,
			ieOptions::addCommandSwitches);

		_setBooleanOption(
			InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, value));
		_setBooleanOption(
			InternetExplorerDriver.NATIVE_EVENTS, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.NATIVE_EVENTS, value));
		_setBooleanOption(
			InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, value));
		_setBooleanOption(
			InternetExplorerDriver.IGNORE_ZOOM_SETTING, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.IGNORE_ZOOM_SETTING, value));
		_setBooleanOption(
			InternetExplorerDriver.
				INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
			properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.
					INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
				value));
		_setBooleanOption(
			InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, value));
		_setBooleanOption(
			"ie.enableFullPageScreenshot", properties,
			(value) -> ieOptions.setCapability(
				"ie.enableFullPageScreenshot", value));
		_setBooleanOption(
			InternetExplorerDriver.FORCE_CREATE_PROCESS, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.FORCE_CREATE_PROCESS, value));
		_setBooleanOption(
			InternetExplorerDriver.IE_USE_PER_PROCESS_PROXY, properties,
			(value) -> ieOptions.setCapability(
				InternetExplorerDriver.IE_USE_PER_PROCESS_PROXY, value));
		_setBooleanOption(
			"ie.forceShellWindowsApi", properties,
			(value) -> ieOptions.setCapability(
				"ie.forceShellWindowsApi", value));

		_setEnumOption(
			InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, properties,
			ieOptions::elementScrollTo, ElementScrollBehavior::valueOf);
		_setEnumOption(
			CapabilityType.PAGE_LOAD_STRATEGY, properties,
			ieOptions::setPageLoadStrategy, PageLoadStrategy::valueOf);
		_setEnumOption(
			CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
			properties, ieOptions::setUnhandledPromptBehaviour,
			UnexpectedAlertBehaviour::valueOf);

		_setLongOption(
			"ie.fileUploadDialogTimeout", properties,
			(value) -> ieOptions.waitForUploadDialogUpTo(
				value, TimeUnit.MILLISECONDS));
		_setLongOption(
			InternetExplorerDriver.BROWSER_ATTACH_TIMEOUT, properties,
			(value) -> ieOptions.withAttachTimeout(
				value, TimeUnit.MILLISECONDS));

		_setKeyValueStringOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CAPABILITY_PREFIX, properties,
			ieOptions::setCapability);

		_setProxy(properties, ieOptions::setProxy);

		_setStringOption(
			InternetExplorerDriver.INITIAL_BROWSER_URL, properties,
			ieOptions::withInitialBrowserUrl);

		return ieOptions;
	}

	public static Capabilities getCapabilities(
		String propertyPrefix, DesiredCapabilities desiredCapabilities) {

		Map<String, String> properties =
			SeleniumProperties.getByPrefix(propertyPrefix);

		// default to false
		desiredCapabilities.setJavascriptEnabled(false);

		_setBooleanOption(
			CapabilityType.ACCEPT_INSECURE_CERTS, properties,
			desiredCapabilities::setAcceptInsecureCerts);
		_setBooleanOption(
			CapabilityType.SUPPORTS_JAVASCRIPT, properties,
			desiredCapabilities::setJavascriptEnabled);

		_setEnumOption(
			CapabilityType.PLATFORM_NAME, properties,
			desiredCapabilities::setPlatform, Platform::valueOf);

		_setStringOption(
			CapabilityType.BROWSER_NAME, properties,
			desiredCapabilities::setBrowserName);
		_setStringOption(
			CapabilityType.BROWSER_VERSION, properties,
			desiredCapabilities::setVersion);

		_setKeyValueStringOptions(
			SeleniumPropertyKeys.BROWSER_OPTIONS_CAPABILITY_PREFIX, properties,
			desiredCapabilities::setCapability);

		return desiredCapabilities;
	}

	private static void _setArguments(
		String propertyKey, Map<String, String> properties,
		Consumer<String[]> argumentConsumer) {

		if (properties.containsKey(propertyKey)) {
			String arguments = properties.get(propertyKey);

			if ((arguments == null) || arguments.isEmpty()) {
				return;
			}

			argumentConsumer.accept(arguments.split(","));
		}
	}

	private static void _setBooleanOption(
		String propertyKey, Map<String, String> properties,
		Consumer<Boolean> booleanConsumer) {

		Consumer<String> adaptor = (value) ->
			booleanConsumer.accept(Boolean.valueOf(value));

		_setStringOption(propertyKey, properties, adaptor);
	}

	private static <E extends Enum<E>> void _setEnumOption(
		String propertyKey, Map<String, String> properties,
		Consumer<E> enumConsumer, Function<String, E> enumFunction) {

		String enumString = properties.get(propertyKey);

		try {
			E e = enumFunction.apply(enumString.toUpperCase());

			enumConsumer.accept(e);
		}
		catch (NullPointerException | IllegalArgumentException iae) {
			// property didn't match enum value
		}
	}

	private static void _setFileOptions(
		Map<String, String> properties, Consumer<File[]> argumentConsumer) {

		if (properties.containsKey(
				SeleniumPropertyKeys.BROWSER_OPTIONS_EXTENSIONS)) {

			String extensions =
				properties.get(SeleniumPropertyKeys.BROWSER_OPTIONS_EXTENSIONS);

			if ((extensions == null) || extensions.isEmpty()) {
				return;
			}

			String[] filePaths = extensions.split(",");

			if (filePaths.length == 0) {
				return;
			}

			File[] files = new File[filePaths.length];

			for (int i = 0; i < filePaths.length; i++) {
				files[i] = new File(filePaths[i]);
			}

			argumentConsumer.accept(files);
		}
	}

	private static void _setIntegerOption(
		String propertyKey, Map<String, String> properties,
		Consumer<Integer> integerConsumer) {

		Consumer<String> adaptor = (value) -> {
			try {
				int propertyValue = Integer.parseInt(value);

				integerConsumer.accept(propertyValue);
			}
			catch (NumberFormatException nfe) {
				// ignore property
			}
		};

		_setStringOption(propertyKey, properties, adaptor);
	}

	private static void _setKeyValueBooleanOptions(
		String innerPropertyPrefix, Map<String, String> properties,
		BiConsumer<String, Boolean> multiOptionConsumer) {

		BiConsumer<String, String> adaptor = (key, value) ->
			multiOptionConsumer.accept(key, Boolean.valueOf(value));

		_setKeyValueStringOptions(innerPropertyPrefix, properties, adaptor);
	}

	private static void _setKeyValueIntOptions(
		String innerPropertyPrefix, Map<String, String> properties,
		BiConsumer<String, Integer> multiOptionConsumer) {

		BiConsumer<String, String> adaptor = (key, value) -> {
			try {
				int propertyValue = Integer.parseInt(value);

				multiOptionConsumer.accept(key, propertyValue);
			}
			catch (NumberFormatException nfe) {
				// ignore property
			}
		};

		_setKeyValueStringOptions(innerPropertyPrefix, properties, adaptor);
	}

	private static void _setKeyValueStringOptions(
		String innerPropertyPrefix, Map<String, String> properties,
		BiConsumer<String, String> multiOptionConsumer) {

		for (Map.Entry<String, String> propertyEntry : properties.entrySet()) {
			String propertyKey = propertyEntry.getKey();

			if (!propertyKey.startsWith(innerPropertyPrefix)) {
				continue;
			}

			propertyKey = propertyKey.substring(innerPropertyPrefix.length());

			multiOptionConsumer.accept(propertyKey, propertyEntry.getValue());
		}
	}

	private static void _setLongOption(
		String propertyKey, Map<String, String> properties,
		Consumer<Long> integerConsumer) {

		Consumer<String> adaptor = (value) -> {
			try {
				long propertyValue = Long.parseLong(value);

				integerConsumer.accept(propertyValue);
			}
			catch (NumberFormatException nfe) {
				// ignore property
			}
		};

		_setStringOption(propertyKey, properties, adaptor);
	}

	private static void _setProfile(
		Map<String, String> properties,
		Consumer<FirefoxProfile> profileConsumer) {

		//TODO::process profile settings
	}

	private static void _setProxy(
		Map<String, String> properties, Consumer<Proxy> proxyConsumer) {

		if (!properties.containsKey(
				SeleniumPropertyKeys.BROWSER_OPTIONS_PROXY_ENABLED) ||
			!Boolean.valueOf(
				properties.get(
					SeleniumPropertyKeys.BROWSER_OPTIONS_PROXY_ENABLED))) {

			return;
		}

		Proxy proxy = new Proxy();

		_setEnumOption(
			"proxy.proxyType", properties, proxy::setProxyType, Proxy.ProxyType::valueOf);

		_setBooleanOption("proxy.auto.detect", properties, proxy::setAutodetect);

		_setStringOption("proxy.ftp", properties, proxy::setFtpProxy);
		_setStringOption("proxy.http", properties, proxy::setHttpProxy);
		_setStringOption("proxy.no", properties, proxy::setNoProxy);
		_setStringOption(
			"proxy.autoconfig.url", properties, proxy::setProxyAutoconfigUrl);
		_setStringOption(
			"proxy.socks.password", properties, proxy::setSocksPassword);
		_setStringOption("proxy.socks", properties, proxy::setSocksProxy);
		_setStringOption(
			"proxy.socks.username", properties, proxy::setSocksUsername);
		_setIntegerOption(
			"proxy.socks.version", properties, proxy::setSocksVersion);
		_setStringOption("proxy.ssl", properties, proxy::setSslProxy);

		proxyConsumer.accept(proxy);
	}

	private static void _setStringOption(
		String propertyKey, Map<String, String> properties,
		Consumer<String> booleanConsumer) {

		if (properties.containsKey(propertyKey)) {
			booleanConsumer.accept(
				properties.get(propertyKey));
		}
	}

}