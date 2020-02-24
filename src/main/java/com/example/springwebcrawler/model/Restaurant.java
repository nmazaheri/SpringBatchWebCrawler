package com.example.springwebcrawler.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@IdClass(RestaurantId.class)
@Table(indexes = {
		@Index(name = "JOB_IDX", columnList = "jobId")
})
public class Restaurant {

	@Id
	private Integer id;
	@Id
	private Integer jobId;
	private String name;
	private String deliveryCost;
	private String deliveryTimeMinutes;
	private String reviewCount;
	private String detailsUrl;
	private String cuisines;
	private String address;
	private String longitude;
	private String latitude;
	private String website;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeliveryCost() {
		return deliveryCost;
	}

	public void setDeliveryCost(String deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	public String getDeliveryTimeMinutes() {
		return deliveryTimeMinutes;
	}

	public void setDeliveryTimeMinutes(String deliveryTimeMinutes) {
		this.deliveryTimeMinutes = deliveryTimeMinutes;
	}

	public String getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(String reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getDetailsUrl() {
		return detailsUrl;
	}

	public void setDetailsUrl(String detailsUrl) {
		this.detailsUrl = detailsUrl;
	}

	public String getCuisines() {
		return cuisines;
	}

	public void setCuisines(String cuisines) {
		this.cuisines = cuisines;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id='" + id + '\'' +
				", jobId=" + jobId +
				", name='" + name + '\'' +
				", deliveryCost='" + deliveryCost + '\'' +
				", deliveryTimeMinutes='" + deliveryTimeMinutes + '\'' +
				", reviewCount='" + reviewCount + '\'' +
				", detailsUrl='" + detailsUrl + '\'' +
				", cuisines='" + cuisines + '\'' +
				", address='" + address + '\'' +
				", longitude='" + longitude + '\'' +
				", latitude='" + latitude + '\'' +
				", website='" + website + '\'' +
				'}';
	}
}
