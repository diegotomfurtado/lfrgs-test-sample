package com.liferay.gs.test.functional.selenium.constants;

/**
 * @author Andrew Betts
 */
public class Environment {

	public static boolean is64Bit() {
		String bit = System.getProperty("sun.arch.data.model");

		switch (bit) {
			case "32":
				return false;
			case "64":
				return true;
			default:
				throw new IllegalStateException(
					"unknown environment; please set \"sun.arch.data.model\"");
		}
	}

}
