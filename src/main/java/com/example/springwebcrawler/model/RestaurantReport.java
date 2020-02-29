package com.example.springwebcrawler.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class RestaurantReport {

	@Id
	private Long jobId;
	private Integer restaurantCount;
	private Integer uniqueZipCodeCount;
	private String mostPopularCuisine;
	private Integer mostPopularCuisineCount;
	private Integer restaurantsWithWebsite;
	@OneToOne(targetEntity = DistributionSummary.class, cascade = CascadeType.ALL)
	private DistributionSummary deliveryFeeDistribution;
	@OneToOne(targetEntity = DistributionSummary.class, cascade = CascadeType.ALL)
	private DistributionSummary deliveryTimeDistribution;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Integer getRestaurantCount() {
		return restaurantCount;
	}

	public void setRestaurantCount(Integer restaurantCount) {
		this.restaurantCount = restaurantCount;
	}

	public Integer getUniqueZipCodeCount() {
		return uniqueZipCodeCount;
	}

	public void setUniqueZipCodeCount(Integer uniqueZipcodeCount) {
		this.uniqueZipCodeCount = uniqueZipcodeCount;
	}

	public String getMostPopularCuisine() {
		return mostPopularCuisine;
	}

	public void setMostPopularCuisine(String mostPopularCuisine) {
		this.mostPopularCuisine = mostPopularCuisine;
	}

	public Integer getMostPopularCuisineCount() {
		return mostPopularCuisineCount;
	}

	public void setMostPopularCuisineCount(Integer mostPopularCuisineCount) {
		this.mostPopularCuisineCount = mostPopularCuisineCount;
	}

	public Integer getRestaurantsWithWebsite() {
		return restaurantsWithWebsite;
	}

	public void setRestaurantsWithWebsite(Integer restaurantsWithWebsite) {
		this.restaurantsWithWebsite = restaurantsWithWebsite;
	}

	public DistributionSummary getDeliveryFeeDistribution() {
		return deliveryFeeDistribution;
	}

	public void setDeliveryFeeDistribution(
			DistributionSummary deliveryFeeDistribution) {
		this.deliveryFeeDistribution = deliveryFeeDistribution;
	}

	public DistributionSummary getDeliveryTimeDistribution() {
		return deliveryTimeDistribution;
	}

	public void setDeliveryTimeDistribution(
			DistributionSummary deliveryTimeDistribution) {
		this.deliveryTimeDistribution = deliveryTimeDistribution;
	}
}
