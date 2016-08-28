package com.tqn.demo.batch.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class RemoteJobExecutionListener implements JobExecutionListener {
	private static final Log logger = LogFactory.getLog(RemoteJobExecutionListener.class);

	public void beforeJob(JobExecution jobExecution) {
		logger.info("********** beforeJob ***********");
	}

	public void afterJob(JobExecution jobExecution) {
		logger.info("********** afterJob ***********");
	}

}
