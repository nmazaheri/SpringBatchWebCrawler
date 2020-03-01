package com.example.springwebcrawler.batch;

import com.example.springwebcrawler.model.Restaurant;
import com.example.springwebcrawler.util.CrawlerUtils;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

public class TakeawayWebScraper implements ItemReader<Restaurant> {

	private static final Logger log = LoggerFactory.getLogger(TakeawayWebScraper.class);

	private URI uri;
	private Long jobId;
	private List<Element> restaurants;
	private AtomicInteger index;

	public TakeawayWebScraper(String url, Long jobId) throws IOException, URISyntaxException {
		this.jobId = jobId;
		this.uri = new URI(url);
		log.debug("retrieving dom for {}", url);
		Document document = CrawlerUtils.getConnection(url).get();
		restaurants = document.getElementsByClass("js-restaurant restaurant ");
		index = new AtomicInteger(0);
	}

	private Restaurant parseElement(Element restaurantElement, Integer index) {
		log.trace("scraper result {}", restaurantElement);
		String review = restaurantElement.getElementsByClass("rating-total").text();

		Element details = restaurantElement.getElementsByClass("detailswrapper").first();
		Element header = details.getElementsByTag("a").first();

		String name = header.text();
		String url = header.attr("href");
		String cuisines = details.getElementsByClass("kitchens").text();

		Element deliveryBar = details.getElementsByClass("details").first();
		String deliveryCost = deliveryBar.getElementsByClass("delivery-cost").text();
		String deliveryTime = deliveryBar.getElementsByClass("avgdeliverytimefull").text();

		Restaurant restaurant = new Restaurant();
		restaurant.setJobId(jobId);
		restaurant.setId(index);
		restaurant.setName(name);
		String detailsUrl = getDetailsUrl(url);
		restaurant.setDetailsUrl(detailsUrl);
		restaurant.setCuisines(cuisines);
		restaurant.setDeliveryCost(deliveryCost);
		restaurant.setDeliveryTimeMinutes(deliveryTime);
		restaurant.setReviewCount(review);
		log.debug("creating {}", restaurant);
		return restaurant;
	}

	private String getDetailsUrl(String url) {
		return this.uri.getScheme() + "://" + this.uri.getHost() + url;
	}

	@Override
	public Restaurant read() {
		int id = this.index.getAndIncrement();
		if (restaurants.size() > id) {
			Element element = restaurants.get(id);
			return parseElement(element, id);
		}
		return null;
	}

}