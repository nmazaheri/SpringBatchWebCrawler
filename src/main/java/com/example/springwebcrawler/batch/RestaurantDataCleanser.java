package com.example.springwebcrawler.batch;

import com.example.springwebcrawler.model.Restaurant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDataCleanser implements ItemProcessor<Restaurant, Restaurant> {

	private static final Logger log = LoggerFactory.getLogger(RestaurantDataCleanser.class);

	@Override
	public Restaurant process(Restaurant restaurant) {
		log.debug("cleaning {}", restaurant);
		cleanReview(restaurant);
		cleanDeliveryCost(restaurant);
		cleanDeliveryTime(restaurant);
		log.trace("cleaned {}", restaurant);
		return restaurant;
	}

	static void cleanReview(Restaurant restaurant) {
		String updatedReview = restaurant.getReviewCount();
		if (StringUtils.isNumeric(updatedReview)) {
			return;
		}
		updatedReview = updatedReview.substring(1, updatedReview.length() - 1);
		restaurant.setReviewCount(updatedReview);
	}

	static void cleanDeliveryCost(Restaurant restaurant) {
		String deliveryCost = restaurant.getDeliveryCost();
		if (StringUtils.isBlank(deliveryCost)) {
			return;
		}
		deliveryCost = deliveryCost.trim();
		if (deliveryCost.equals("FREE")) {
			restaurant.setDeliveryCost("0");
		} else if (!deliveryCost.startsWith("€")) {
			log.info("skipping delivery cost: {}", restaurant);
		} else {
			String newDeliveryCost = deliveryCost.substring(1).replace(',', '.').trim();
			restaurant.setDeliveryCost(newDeliveryCost);
		}
	}

	static void cleanDeliveryTime(Restaurant restaurant) {
		String deliveryTime = restaurant.getDeliveryTimeMinutes();
		if (StringUtils.isBlank(deliveryTime)) {
			return;
		}
		if (deliveryTime.startsWith("Closed") || deliveryTime.startsWith("From")) {
			restaurant.setDeliveryTimeMinutes(null);
			return;
		}
		if (!deliveryTime.startsWith("est")) {
			log.info("skipping delivery time: {}", restaurant);
			return;
		}
		deliveryTime = deliveryTime.substring(4, deliveryTime.length() - 3);
		restaurant.setDeliveryTimeMinutes(deliveryTime);
	}
}
