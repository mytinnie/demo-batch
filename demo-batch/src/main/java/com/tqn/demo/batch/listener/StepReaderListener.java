package com.tqn.demo.batch.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;

public class StepReaderListener implements StepExecutionListener {
	//private static final Logger logger = Logger.getLogger(StepReaderListener.class);
	private static final Log logger = LogFactory.getLog(StepReaderListener.class);
	
	public void beforeStep(StepExecution stepExecution) {
		logger.info("********** beforeStep ***********");
		ExecutionContext executionContext = stepExecution.getExecutionContext();
//		String fileName = executionContext.getString("fileName");
//		String outputFile = fileName.replace("inbox", "outbox");
//		executionContext.putString("outputFile", outputFile);
		logger.info(executionContext);
	}

	public ExitStatus afterStep(StepExecution stepExecution) {
		logger.info("********** afterStep ***********");
//		String fileName = stepExecution.getExecutionContext().getString("fileName").replace("file:/", "");
//		String outputFile = fileName.replace("inbox", "archivebox");
//	    try {
//			Files.move(Paths.get(fileName), Paths.get(outputFile), StandardCopyOption.REPLACE_EXISTING);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return stepExecution.getExitStatus();
	}

}
