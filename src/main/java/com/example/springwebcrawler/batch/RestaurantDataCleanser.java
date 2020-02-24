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

	private void cleanReview(Restaurant restaurant) {
		String updatedReview = restaurant.getReviewCount();
		log.trace("cleaning updatedReview count: {}", updatedReview);
		if (StringUtils.isNumeric(updatedReview)) {
			updatedReview = updatedReview.trim();
		} else {
			updatedReview = updatedReview.substring(1, updatedReview.length() - 1);
		}
		restaurant.setReviewCount(updatedReview);
	}

	private void cleanDeliveryCost(Restaurant restaurant) {
		String deliveryCost = restaurant.getDeliveryCost();
		log.trace("cleaning delivery cost: {}", deliveryCost);
		if (StringUtils.isBlank(deliveryCost) || StringUtils.isNumeric(deliveryCost)) {
			log.warn("delivery cost is {}", deliveryCost);
			return;
		}
		if (deliveryCost.equals("FREE")) {
			restaurant.setDeliveryCost("0");
		} else {
			try {
				String newDeliveryCost = deliveryCost.substring(2).replace(',', '.');
				restaurant.setDeliveryCost(newDeliveryCost);
			} catch (StringIndexOutOfBoundsException e) {
				log.warn("cannot parse deliveryCost for {}", restaurant);
			}
		}
	}

	private void cleanDeliveryTime(Restaurant restaurant) {
		String deliveryTime = restaurant.getDeliveryTimeMinutes();
		log.trace("cleaning delivery time: {}", deliveryTime);
		if (StringUtils.isBlank(deliveryTime) || StringUtils.isNumeric(deliveryTime)) {
			log.warn("delivery time is {}", deliveryTime);
			return;
		}
		if (deliveryTime.startsWith("Closed") || deliveryTime.startsWith("From")) {
			restaurant.setDeliveryTimeMinutes(null);
		} else {
			try {
				deliveryTime = deliveryTime.substring(4, deliveryTime.length() - 3);
				restaurant.setDeliveryTimeMinutes(deliveryTime);
			} catch (StringIndexOutOfBoundsException e) {
				log.warn("cannot parse deliveryTime for {}", restaurant);
			}
		}
	}

}
