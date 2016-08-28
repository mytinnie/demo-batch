package com.tqn.demo.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;

@Configuration
@PropertySource(ignoreResourceNotFound=true,value="classpath:ftp-${spring.profiles.active}.properties")
public class FTPFileConfiguration {
	private static final Log logger = LogFactory.getLog(FTPFileConfiguration.class);
	@Autowired
	private Environment environment;

	@Bean
	public FtpInboundFileSynchronizer remoteInboundFileSynchronizer() {
		logger.info("remoteInboundFileSynchronizer");
		FtpInboundFileSynchronizer synchronizer = new FtpInboundFileSynchronizer(ftpSessionFactory());
		synchronizer.setRemoteDirectory(environment.getProperty("ftp.get.directory"));
		synchronizer.setFilter(new FtpSimplePatternFileListFilter(environment.getProperty("ftp.filelistfilter")));
		synchronizer.setIntegrationEvaluationContext(new StandardEvaluationContext());
		return synchronizer;
	}

	@Bean
	public DefaultFtpSessionFactory ftpSessionFactory() {
		DefaultFtpSessionFactory factory = new DefaultFtpSessionFactory();
		factory.setHost(environment.getProperty("ftp.host"));
		factory.setPort(Integer.parseInt(environment.getProperty("ftp.port")));
		factory.setUsername(environment.getProperty("ftp.username"));
		factory.setPassword(environment.getProperty("ftp.password"));
		factory.setClientMode(Integer.parseInt(environment.getProperty("ftp.clientmode")));
		factory.setFileType(Integer.parseInt(environment.getProperty("ftp.filetype")));
		return factory;
	}
}
