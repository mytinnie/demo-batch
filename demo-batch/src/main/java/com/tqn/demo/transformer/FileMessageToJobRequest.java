package com.tqn.demo.transformer;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

// transform a file into a JobLaunchRequest
public class FileMessageToJobRequest {
	private static final Log logger = LogFactory.getLog(FileMessageToJobRequest.class);
	final static String TIMEPARAM = "time";
    private Job job;
    private String fileParameterName;

    public void setFileParameterName(String fileParameterName) {
        this.fileParameterName = fileParameterName;
    }
    public void setJob(Job job) {
        this.job = job;
    }

    @Transformer
    public JobLaunchRequest toRequest(Message<File> message) {
    	logger.info("JobLaunchRequest.toRequest " + message.getPayload().getAbsolutePath());
    	logger.info("JobLaunchRequest.toRequest " + message.getPayload().getName());
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(fileParameterName, message.getPayload().getName());
        jobParametersBuilder.addLong(TIMEPARAM, System.currentTimeMillis());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }
}
