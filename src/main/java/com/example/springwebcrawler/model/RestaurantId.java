package com.example.springwebcrawler.model;

import java.io.Serializable;
import java.util.Objects;

public class RestaurantId implements Serializable {

	private Integer id;
	private Integer jobId;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RestaurantId that = (RestaurantId) o;
		return id.equals(that.id) &&
				jobId.equals(that.jobId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, jobId);
	}
}
