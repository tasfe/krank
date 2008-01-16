package org.crank.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** To expose as EL expression functions. */
public class StringUtils {
	public static boolean contains(String str, String substring) {
		return str.contains(substring);
	}

	public static String replace(String str, String oldStr, String newStr) {
		return str.replace(oldStr, newStr);
	}

	public static List<String> split(String str) {
		return Arrays.asList(str.split(","));
	}

	public static List<String> splitProperty(String str) {

		int dotIndex = str.indexOf(".");
		String prop1 = str.substring(0, dotIndex);
		String prop2 = str.substring(dotIndex + 1);

		List<String> results = new ArrayList<String>();

		results.add(prop1);
		results.add(prop2);

		return results;

	}

	public static String unCapitalize(String string) {
		StringBuilder rv = new StringBuilder();
		if (string.length() > 0) {
			rv.append(Character.toLowerCase(string.charAt(0)));
			if (string.length() > 1) {
				rv.append(string.substring(1));
			}
		}
		return rv.toString();
	}

	public static String capitalize(String string) {
		StringBuilder rv = new StringBuilder();
		if (string.length() > 0) {
			rv.append(Character.toUpperCase(string.charAt(0)));
			if (string.length() > 1) {
				rv.append(string.substring(1));
			}
		}
		return rv.toString();
	}

}
