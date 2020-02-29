package com.example.springwebcrawler.model;

import org.springframework.batch.core.StepExecution;

public class JobStatus {

	private long stepId;
	private int writeCount;
	private int readCount;
	private int commitCount;
	private int rollbackCount;
	private int readSkipCount;
	private int processSkipCount;
	private int writeSkipCount;

	public JobStatus(StepExecution execution) {
		stepId = execution.getId();
		writeCount = execution.getWriteCount();
		readCount = execution.getReadCount();
		commitCount = execution.getCommitCount();
		rollbackCount = execution.getRollbackCount();
		readSkipCount = execution.getReadSkipCount();
		processSkipCount = execution.getProcessSkipCount();
		writeSkipCount = execution.getWriteSkipCount();
	}
}
