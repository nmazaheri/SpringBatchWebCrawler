package com.example.springwebcrawler.config;

import com.example.springwebcrawler.batch.RestaurantReportTasklet;
import com.example.springwebcrawler.repository.RestaurantReportRepository;
import com.example.springwebcrawler.repository.RestaurantRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestaurantReportConfig {

	@Bean
	public Step generateRestaurantReport(
			StepBuilderFactory stepBuilderFactory, RestaurantReportTasklet restaurantReportTaskletBean) {
		return stepBuilderFactory.get("GenerateRestaurantReport")
				.tasklet(restaurantReportTaskletBean)
				.build();
	}

	@Bean
	public RestaurantReportTasklet restaurantReportTaskletBean(
			RestaurantRepository restaurantRepository,
			RestaurantReportRepository restaurantReportRepository) {
		return new RestaurantReportTasklet(restaurantRepository, restaurantReportRepository);
	}
}
