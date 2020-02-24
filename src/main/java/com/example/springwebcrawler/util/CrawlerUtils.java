package com.example.springwebcrawler.util;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class CrawlerUtils {

	private static final String USER_AGENT = "Mozilla/5.0";
	private static final int CONNECTION_TIMEOUT_MS = 5000;

	public static Connection getConnection(String url) {
		Connection connect = Jsoup.connect(url);
		connect.timeout(CONNECTION_TIMEOUT_MS);
		connect.userAgent(USER_AGENT);
		return connect;
	}
}
