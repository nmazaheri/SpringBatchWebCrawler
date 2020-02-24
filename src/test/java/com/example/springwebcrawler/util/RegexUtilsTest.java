package com.example.springwebcrawler.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RegexUtilsTest {

	@Test
	public void testPostcode() {
		String address = "Sint Jorisstraat 4 H 1017BC Amsterdam";
		String postcode = RegexUtils.extractPostcode(address);
		assertEquals("1017BC", postcode);
	}

}