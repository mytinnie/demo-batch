package com.tqn.demo.batch.tasklet;

import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.integration.file.remote.synchronizer.AbstractInboundFileSynchronizer;

public class RemoteInboundFilesTasklet implements InitializingBean, Tasklet {
	private static final Log logger = LogFactory.getLog(RemoteInboundFilesTasklet.class);
	private Resource localDirectory;
	private AbstractInboundFileSynchronizer fileSynchronizer;

	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.info("*************** RemoteInboundFilesTasklet ***************");

		// remove files in local directory???
		
		// sync 
		fileSynchronizer.synchronizeToLocalDirectory(this.localDirectory.getFile());
				
		return RepeatStatus.FINISHED;
	}

	public void afterPropertiesSet() throws Exception {
		// check local directory
		if (!this.localDirectory.exists()) {
			throw new FileNotFoundException(this.localDirectory.getFilename());
		}
	}

	public void setFileSynchronizer(AbstractInboundFileSynchronizer fileSynchronizer) {
		this.fileSynchronizer = fileSynchronizer;
	}

	public void setLocalDirectory(Resource localDirectory) {
		this.localDirectory = localDirectory;
	}
}
