package com.example.springwebcrawler.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.springwebcrawler.model.Restaurant;
import org.junit.jupiter.api.Test;

class RestaurantDataCleanserTest {

	@Test
	void testCleanReview() {
		Restaurant restaurant = new Restaurant();
		restaurant.setReviewCount("(300)");
		RestaurantDataCleanser.cleanReview(restaurant);
		String actual = restaurant.getReviewCount();
		assertEquals("300", actual);
	}

	@Test
	void testCleanDeliveryCost() {
		Restaurant restaurant = new Restaurant();
		restaurant.setDeliveryCost("€ 2,50");
		RestaurantDataCleanser.cleanDeliveryCost(restaurant);
		String actual = restaurant.getDeliveryCost();
		assertEquals("2.50", actual);
	}

	@Test
	void testCleanDeliveryTime() {
		Restaurant restaurant = new Restaurant();
		restaurant.setDeliveryTimeMinutes("est.35min");
		RestaurantDataCleanser.cleanDeliveryTime(restaurant);
		String actual = restaurant.getDeliveryTimeMinutes();
		assertEquals("35", actual);
	}

}