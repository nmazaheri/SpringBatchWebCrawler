package com.example.springwebcrawler.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;

@Entity
public class DistributionSummary {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Double average;
	private Double standardDeviation;
	private Double variance;
	private Double minimum;
	private Double maximum;

	public void setStatisticsSummary(StatisticalSummary summary) {
		setAverage(summary.getMean());
		setMinimum(summary.getMin());
		setMaximum(summary.getMax());
		setStandardDeviation(summary.getStandardDeviation());
		setVariance(summary.getVariance());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public Double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(Double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public Double getVariance() {
		return variance;
	}

	public void setVariance(Double variance) {
		this.variance = variance;
	}

	public Double getMinimum() {
		return minimum;
	}

	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}

	public Double getMaximum() {
		return maximum;
	}

	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}
}
