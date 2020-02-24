package com.example.springwebcrawler.batch;

import com.example.springwebcrawler.model.Restaurant;
import com.example.springwebcrawler.repository.RestaurantRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class RestaurantDBWriter implements ItemWriter<Restaurant> {

	private static final Logger log = LoggerFactory.getLogger(RestaurantDBWriter.class);
	private final RestaurantRepository restaurantRepository;

	public RestaurantDBWriter(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Override
	synchronized public void write(List<? extends Restaurant> restaurants) {
		log.debug("Storing {} Restaurants to DB for jobId={}", restaurants.size(),
				restaurants.get(0).getJobId());
		restaurantRepository.saveAll(restaurants);
	}
}