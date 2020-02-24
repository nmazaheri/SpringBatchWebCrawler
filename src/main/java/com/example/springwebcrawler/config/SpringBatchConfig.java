package com.example.springwebcrawler.config;

import com.example.springwebcrawler.batch.RestaurantDataCleanser;
import com.example.springwebcrawler.batch.RestaurantGlobalIdentifier;
import com.example.springwebcrawler.batch.RestaurantReportTasklet;
import com.example.springwebcrawler.batch.TakeawayRestaurantWebScraper;
import com.example.springwebcrawler.batch.TakeawayWebScraper;
import com.example.springwebcrawler.model.Restaurant;
import com.example.springwebcrawler.repository.RestaurantReportRepository;
import com.example.springwebcrawler.repository.RestaurantRepository;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

@SuppressWarnings({"unchecked", "rawtypes", "SpringElInspection"})
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

	private static final int RETRY_LIMIT = 10;
	private static final int CHUNK_SIZE = 50;
	private static final int MAX_JOB_THREADS = 3;

	@Bean
	public Job takeawayJob(JobBuilderFactory jobBuilderFactory, Step retrieveTakeawayData,
			Step generateRestaurantReport) {
		return jobBuilderFactory.get("TakeawayJob")
				.incrementer(new RunIdIncrementer())
				.start(retrieveTakeawayData)
				.next(generateRestaurantReport)
				.build();
	}

	@Bean
	public Step retrieveTakeawayData(
			StepBuilderFactory stepBuilderFactory,
			ItemReader<Restaurant> TakeawayWebScraperBean,
			ItemWriter<Restaurant> restaurantDBWriter,
			CompositeItemProcessor takeawayRestaurantProcessor,
			TaskExecutor jobTaskExecutor) {

		return stepBuilderFactory.get("RetrieveTakeawayData")
//				.allowStartIfComplete(true) // TODO investigate this functionality
				.<Restaurant, Restaurant>chunk(CHUNK_SIZE)
				.reader(TakeawayWebScraperBean)
				.processor(takeawayRestaurantProcessor).faultTolerant().retryLimit(RETRY_LIMIT)
				.backOffPolicy(new ExponentialBackOffPolicy()).retry(SocketTimeoutException.class)
				.writer(restaurantDBWriter)
				.taskExecutor(jobTaskExecutor)
				.build();
	}

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

	@Bean
	public CompositeItemProcessor takeawayRestaurantProcessor(
			RestaurantGlobalIdentifier restaurantIdentifier) {
		CompositeItemProcessor processor = new CompositeItemProcessor();
		processor.setDelegates(
				Arrays.asList(new TakeawayRestaurantWebScraper(), restaurantIdentifier,
						new RestaurantDataCleanser()));
		return processor;
	}

	@Bean
	@StepScope
	public RestaurantGlobalIdentifier restaurantIdentifier(
			@Value("#{jobParameters[apiKey]}") String apiKey) {
		return new RestaurantGlobalIdentifier(apiKey);
	}

	@Bean
	public TaskExecutor jobTaskExecutor() {
		SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleAsyncTaskExecutor.setConcurrencyLimit(MAX_JOB_THREADS);
		return simpleAsyncTaskExecutor;
	}

	@Bean
	@Primary
	public JobLauncher JobLauncher(JobRepository jobRepository) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	@Bean
	@StepScope
	public ItemReader<Restaurant> TakeawayWebScraperBean(
			@Value("#{stepExecution.getJobExecutionId()}") Integer jobId,
			@Value("#{jobParameters[url]}") String url) throws IOException, URISyntaxException {
		return new TakeawayWebScraper(url, jobId);
	}
}