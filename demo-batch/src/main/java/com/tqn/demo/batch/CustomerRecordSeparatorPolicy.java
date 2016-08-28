package com.tqn.demo.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

public class CustomerRecordSeparatorPolicy implements RecordSeparatorPolicy {
	private static final Log logger = LogFactory.getLog(CustomerRecordSeparatorPolicy.class);

	public CustomerRecordSeparatorPolicy() {
	}

	public boolean isEndOfRecord(String line) {
		logger.info("isEndOfRecord: " + line);
		return true;
	}

	public String postProcess(String line) {
		logger.info("postProcess: " + line);
		return line;
	}

	public String preProcess(String line) {
		logger.info("preProcess: " + line);
		return line;
	}

}
