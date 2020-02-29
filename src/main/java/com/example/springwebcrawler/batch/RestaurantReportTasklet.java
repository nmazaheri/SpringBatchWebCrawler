package com.example.springwebcrawler.batch;

import com.example.springwebcrawler.model.DistributionSummary;
import com.example.springwebcrawler.model.Restaurant;
import com.example.springwebcrawler.model.RestaurantReport;
import com.example.springwebcrawler.repository.RestaurantReportRepository;
import com.example.springwebcrawler.repository.RestaurantRepository;
import com.example.springwebcrawler.util.RegexUtils;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class RestaurantReportTasklet implements Tasklet {

	private static final Logger log = LoggerFactory.getLogger(RestaurantReportTasklet.class);
	private RestaurantRepository restaurantRepository;
	private RestaurantReportRepository restaurantReportRepository;

	public RestaurantReportTasklet(RestaurantRepository restaurantRepository,
			RestaurantReportRepository restaurantReportRepository) {
		this.restaurantRepository = restaurantRepository;
		this.restaurantReportRepository = restaurantReportRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		int jobId = contribution.getStepExecution().getJobExecutionId().intValue();
		List<Restaurant> restaurants = restaurantRepository.findByJobId(jobId);

		RestaurantReport restaurantReport = new RestaurantReport();
		restaurantReport.setJobId(jobId);
		restaurantReport.setRestaurantCount(restaurants.size());
		int uniqueZipCodeCount = getUniqueZipCodesCount(restaurants);
		restaurantReport.setUniqueZipCodeCount(uniqueZipCodeCount);

		long websiteCount = restaurants.stream().filter(r -> r.getWebsite() != null).count();
		restaurantReport.setRestaurantsWithWebsite((int) websiteCount);
		setCuisine(restaurants, restaurantReport);

		DistributionSummary deliveryCostEntity = getDeliveryCostDistribution(restaurants);
		restaurantReport.setDeliveryFeeDistribution(deliveryCostEntity);

		DistributionSummary deliveryTimeEntity = getDeliveryTimeDistribution(restaurants);
		restaurantReport.setDeliveryTimeDistribution(deliveryTimeEntity);

		// TODO: add sanity check

		restaurantReportRepository.save(restaurantReport);
		return RepeatStatus.FINISHED;
	}

	private int getUniqueZipCodesCount(List<Restaurant> restaurants) {
		HashSet<String> uniqueZipCodes = new HashSet<>();
		for (Restaurant restaurant : restaurants) {
			String address = restaurant.getAddress();
			String postcode = RegexUtils.extractPostcode(address);
			uniqueZipCodes.add(postcode);
		}
		return uniqueZipCodes.size();
	}

	private DistributionSummary getDeliveryTimeDistribution(List<Restaurant> restaurants) {
		SummaryStatistics deliveryTimes = new SummaryStatistics();
		restaurants.stream().map(Restaurant::getDeliveryTimeMinutes).filter(Objects::nonNull)
				.mapToDouble(Double::parseDouble).forEach(deliveryTimes::addValue);
		DistributionSummary deliveryTimeEntity = new DistributionSummary();
		deliveryTimeEntity.setStatisticsSummary(deliveryTimes);
		return deliveryTimeEntity;
	}

	private DistributionSummary getDeliveryCostDistribution(List<Restaurant> restaurants) {
		SummaryStatistics deliveryCosts = new SummaryStatistics();
		restaurants.stream().mapToDouble(r -> Double.parseDouble(r.getDeliveryCost()))
				.forEach(deliveryCosts::addValue);
		DistributionSummary deliveryCostEntity = new DistributionSummary();
		deliveryCostEntity.setStatisticsSummary(deliveryCosts);
		return deliveryCostEntity;
	}

	private void setCuisine(List<Restaurant> restaurants, RestaurantReport restaurantReport) {
		Map<String, Integer> cuisineCount = getSortedCuisineReviewCount(restaurants);
		Optional<Entry<String, Integer>> largestCuisineCountOptional = cuisineCount.entrySet().stream()
				.findFirst();
		if (largestCuisineCountOptional.isPresent()) {
			Entry<String, Integer> largestCuisineCount = largestCuisineCountOptional.get();
			restaurantReport.setMostPopularCuisine(largestCuisineCount.getKey());
			restaurantReport.setMostPopularCuisineCount(largestCuisineCount.getValue());
		}
	}

	private Map<String, Integer> getSortedCuisineReviewCount(List<Restaurant> restaurants) {
		Map<String, Integer> cuisineReview = getCuisineReviewCount(restaurants);
		LinkedHashMap<String, Integer> sortedResult = cuisineReview.entrySet().stream()
				.sorted(Collections.reverseOrder(Entry.comparingByValue())).collect(
						Collectors.toMap(
								Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		log.debug("sortedCuisineMap: {}", sortedResult);
		return sortedResult;
	}

	private Map<String, Integer> getCuisineReviewCount(List<Restaurant> restaurants) {
		Map<String, Integer> cuisineReview = new HashMap<>();
		restaurants.forEach(restaurant -> {
			int reviewCount = Integer.parseInt(restaurant.getReviewCount());
			String cuisines = restaurant.getCuisines();
			String[] cuisineList = cuisines.split(",");
			Arrays.stream(cuisineList).forEach(cuisine -> {
				String name = cuisine.trim();
				Integer sum = cuisineReview.getOrDefault(name, 0) + reviewCount;
				cuisineReview.put(name, sum);
			});
		});
		return cuisineReview;
	}

}
