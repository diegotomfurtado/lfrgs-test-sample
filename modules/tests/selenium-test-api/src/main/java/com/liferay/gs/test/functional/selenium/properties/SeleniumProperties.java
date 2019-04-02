package com.liferay.gs.test.functional.selenium.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andrew Betts
 */
public class SeleniumProperties {

	private static final Properties _PROPERTIES = new Properties();

	private static void init() throws IOException {

		// copy system properties

		Properties systemProperties = System.getProperties();

		for (String key : systemProperties.stringPropertyNames()) {
			_PROPERTIES.setProperty(key, systemProperties.getProperty(key));
		}

		// load default properties

		ClassLoader classLoader = SeleniumProperties.class.getClassLoader();

		InputStream inputStream =
			classLoader.getResourceAsStream("selenium.properties");

		_PROPERTIES.load(inputStream);

		// override defaults

		String propertiesPath = _PROPERTIES.getProperty(
			SeleniumPropertyKeys.SELENIUM_PROPERTIES_FILE_PATH);

		if ((propertiesPath != null) && !propertiesPath.isEmpty()) {
			_PROPERTIES.load(new FileInputStream(new File(propertiesPath)));
		}
	}

	public static void loadProperties(File propertiesFile)
		throws IOException {

		_PROPERTIES.clear();

		init();

		if (propertiesFile != null) {
			_PROPERTIES.load(new FileInputStream(propertiesFile));
		}
	}

	public static Map<String, String> getByPrefix(String prefix) {
		Map<String, String> properties = new HashMap<>();

		for (String key : _PROPERTIES.stringPropertyNames()) {
			if (!key.startsWith(prefix)) {
				continue;
			}

			properties.put(
				key.substring(prefix.length()),
				_PROPERTIES.getProperty(key));
		}

		return properties;
	}

	public static String get(String key) {
		return _PROPERTIES.getProperty(key);
	}

	public static Boolean getBoolean(String key) {
		return Boolean.valueOf(_PROPERTIES.getProperty(key));
	}

	public static Integer getInteger(String key) {
		try {
			return Integer.parseInt(_PROPERTIES.getProperty(key));
		}
		catch (NumberFormatException nfe) {
			return 0;
		}
	}

	public static Long getLong(String key) {
		try {
			return Long.parseLong(_PROPERTIES.getProperty(key));
		}
		catch (NumberFormatException nfe) {
			return 0L;
		}
	}

	public static void set(String key, String value) {
		_PROPERTIES.setProperty(key, value);
	}

	static {
		try {
			init();
		}
		catch (IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

}