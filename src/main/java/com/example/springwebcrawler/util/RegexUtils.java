package com.example.springwebcrawler.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	private static Pattern ZIP_CODE_PATTERN = Pattern.compile("[0-9]{4}[A-Z]{2}");

	public static String extractPostcode(String address) {
		Matcher matcher = ZIP_CODE_PATTERN.matcher(address);
		boolean found = matcher.find();
		if (!found) {
			throw new IllegalArgumentException("invalid address: " + address);
		}
		return matcher.group(0);
	}

}
