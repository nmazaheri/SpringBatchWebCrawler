package com.example.springwebcrawler.batch;

import com.example.springwebcrawler.model.Restaurant;
import com.example.springwebcrawler.util.CrawlerUtils;
import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TakeawayRestaurantWebScraper implements ItemProcessor<Restaurant, Restaurant> {

	public static final String INFO_URL = "#INFO";
	private static final Logger log = LoggerFactory.getLogger(TakeawayRestaurantWebScraper.class);

	@Override
	public Restaurant process(Restaurant restaurant) throws IOException {
		String infoUrl = restaurant.getDetailsUrl() + INFO_URL;
		log.trace("Requesting dom for {}", infoUrl);
		log.debug("processing {}", restaurant);
		Document document = CrawlerUtils.getConnection(infoUrl).get();
		Elements restaurantElement = document
				.getElementsByClass("info-card restaurant-info__restaurant-link");
		log.trace("dom result {}", restaurantElement);
		if (!restaurantElement.isEmpty()) {
			String website = restaurantElement.first().getElementsByTag("a").attr("href");
			restaurant.setWebsite(website);
		}
		Element address = document.getElementsByClass("card-body").first();
		restaurant.setAddress(address.text());
		log.debug("processed {}", restaurant);
		return restaurant;
	}
}
