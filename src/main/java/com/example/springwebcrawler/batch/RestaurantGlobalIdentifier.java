package com.example.springwebcrawler.batch;

import com.example.springwebcrawler.model.Restaurant;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.DisposableBean;

public class RestaurantGlobalIdentifier implements ItemProcessor<Restaurant, Restaurant>,
		DisposableBean {

	private static final Logger log = LoggerFactory.getLogger(RestaurantGlobalIdentifier.class);
	private GeoApiContext context;

	public RestaurantGlobalIdentifier(String apiKey) {
		if (apiKey == null) {
			return;
		}
		context = new GeoApiContext.Builder()
				.apiKey(apiKey)
				.build();
	}

	@Override
	public Restaurant process(Restaurant restaurant)
			throws InterruptedException, ApiException, IOException {
		if (context == null) {
			return restaurant;
		}
		GeocodingResult[] results = GeocodingApi.geocode(context, restaurant.getAddress()).await();
		if (results.length > 0) {
			updateRestaurant(restaurant, results[0]);
		}
		return restaurant;
	}

	private void updateRestaurant(Restaurant restaurant, GeocodingResult result) {
		LatLng location = result.geometry.location;
		String latitude = String.valueOf(location.lat);
		String longitude = String.valueOf(location.lng);

		restaurant.setLatitude(latitude);
		restaurant.setLongitude(longitude);

		int globalIdentifier = Objects.hash(restaurant.getName(), latitude, longitude);
		restaurant.setId(globalIdentifier);
	}

	@Override
	public void destroy() {
		if (context != null) {
			context.shutdown();
		}
	}
}
