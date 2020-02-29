package com.example.springwebcrawler.controller;

import com.example.springwebcrawler.model.JobResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

	private static final Logger log = LoggerFactory.getLogger(JobController.class);
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private final Job takeawayJob;
	private final JobLauncher jobLauncher;
	private final JobExplorer jobExplorer;

	public JobController(Job takeawayJob, JobLauncher jobLauncher, JobExplorer jobExplorer) {
		this.jobLauncher = jobLauncher;
		this.takeawayJob = takeawayJob;
		this.jobExplorer = jobExplorer;
	}

	@GetMapping("/load")
	public String get(@RequestParam String url, @RequestParam(required = false) String apiKey)
			throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		Map<String, JobParameter> params = new HashMap<>();
		params.put("url", new JobParameter(url));
		params.put("apiKey", new JobParameter(apiKey));
		params.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters parameters = new JobParameters(params);

		//noinspection ConstantConditions
		if (apiKey == null) {
			log.info("No apiKey parameter. Cannot create global unique restaurant IDs");
		}
		JobExecution jobExecution = jobLauncher.run(takeawayJob, parameters);
		JobResponse jobResponse = new JobResponse(jobExecution);
		return gson.toJson(jobResponse);
	}

	@GetMapping("/status")
	public String status(@RequestParam Long jobId) {
		JobExecution jobExecution = jobExplorer.getJobExecution(jobId);
		if (jobExecution == null) {
			return "Not found";
		}
		JobResponse jobResponse = new JobResponse(jobExecution);
		return gson.toJson(jobResponse);
	}
}