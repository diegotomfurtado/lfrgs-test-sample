package com.liferay.gs.test.functional.selenium.xpath;

/**
 * @author Shane Merriss
 */
public class XPathStringBuilder {

	public static String buildXPathContains(
		String tagName, String attributeName, String attributeValue) {

		StringBuilder sb = new StringBuilder();

		sb.append("//");
		sb.append(tagName);
		sb.append("[contains(");
		sb.append(attributeName);
		sb.append(",'");
		sb.append(attributeValue);
		sb.append("')]");

		return sb.toString();
	}

	public static String buildXPathEquals(
		String tagName, String attributeName, String attributeValue) {

		StringBuilder sb = new StringBuilder();

		sb.append("//");
		sb.append(tagName);
		sb.append("[");
		sb.append(attributeName);
		sb.append("='");
		sb.append(attributeValue);
		sb.append("']");

		return sb.toString();
	}

	public static String xpathGoUp(String base, int n) {
		String baseUpString = "/..";

		StringBuilder sb = new StringBuilder();

		sb.append(base);

		for (int i = 0; i < n; i++) {
			sb.append(baseUpString);
		}

		return sb.toString();
	}

}