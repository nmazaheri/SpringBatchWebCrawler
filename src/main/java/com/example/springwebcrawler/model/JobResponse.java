package com.example.springwebcrawler.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;

public class JobResponse {

	private JobInstance jobInstance;
	private BatchStatus status;
	private Date createTime;
	private JobParameters jobParameters;
	private List<JobStatus> jobDetails = new ArrayList<>();

	public JobResponse(JobExecution jobExecution) {
		this.jobInstance = jobExecution.getJobInstance();
		this.status = jobExecution.getStatus();
		this.createTime = jobExecution.getCreateTime();
		this.jobParameters = jobExecution.getJobParameters();
		jobExecution.getStepExecutions().stream().map(JobStatus::new)
				.forEach(jobStatus -> this.jobDetails.add(jobStatus));
	}

	public JobInstance getJobInstance() {
		return jobInstance;
	}

	public BatchStatus getStatus() {
		return status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public JobParameters getJobParameters() {
		return jobParameters;
	}

}
