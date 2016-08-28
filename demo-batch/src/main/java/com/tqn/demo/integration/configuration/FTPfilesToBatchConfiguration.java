package com.tqn.demo.integration.configuration;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.tqn.demo.configuration.FTPFileConfiguration;
import com.tqn.demo.transformer.FileMessageToJobRequest;

@Configuration
@EnableIntegration
@Import({ FTPFileConfiguration.class })
public class FTPfilesToBatchConfiguration {
	private static final Log logger = LogFactory.getLog(FTPfilesToBatchConfiguration.class);
	
	@Autowired
	private FTPFileConfiguration remoteFileConfiguration;
	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private JobRegistry jobRegistry;

	@Value("${ftp.another.get.directory}")
	private String ftpGetDirectory;
	@Value("${ftp.another.filelistfilter}")
	private String ftpListFilter;
	
	@Value("${local.in.file.path}")
	private Resource localDirectoryResource;

	@Bean
	public MessageChannel inboundFtpChannel() {
		return new DirectChannel(); 
	}
	@Bean
	public MessageChannel outboundJobRequestChannel() {
		return new DirectChannel(); 
	}
	@Bean
	public MessageChannel jobLaunchReplyChannel() {
		return new DirectChannel(); 
	}
	
	@Bean
	@InboundChannelAdapter(value = "inboundFtpChannel", poller = @Poller(fixedDelay = "${poller.fixed.delay}"))
	public MessageSource<?> pollFtpForFiles() throws IOException {
		logger.info("pollFtpForFiles");
		FtpInboundFileSynchronizer synchronizer = new FtpInboundFileSynchronizer(remoteFileConfiguration.ftpSessionFactory());
		synchronizer.setRemoteDirectory(ftpGetDirectory);
		synchronizer.setFilter(new FtpSimplePatternFileListFilter(ftpListFilter));
		synchronizer.setIntegrationEvaluationContext(new StandardEvaluationContext());
		FtpInboundFileSynchronizingMessageSource source = new FtpInboundFileSynchronizingMessageSource(synchronizer);
		source.setLocalDirectory(localDirectoryResource.getFile());
		return source; 		
	}
	
	@Bean
	@Transformer(inputChannel = "inboundFtpChannel", outputChannel = "outboundJobRequestChannel")
	public FileMessageToJobRequest transform() throws NoSuchJobException {
		FileMessageToJobRequest transformer =  new FileMessageToJobRequest();
		transformer.setJob(jobRegistry.getJob("LocalfilesToDestinationJob"));
		transformer.setFileParameterName("infilename");
		return transformer;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "outboundJobRequestChannel")
	public JobLaunchingGateway jlg() {
		JobLaunchingGateway gateway = new JobLaunchingGateway(jobLauncher);
		gateway.setOutputChannel(jobLaunchReplyChannel());
		return gateway;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "jobLaunchReplyChannel")
	public MessageHandler logger() {
	     LoggingHandler loggingHandler =  new LoggingHandler(LoggingHandler.Level.WARN.name());
	     //loggingHandler.setLoggerName(name);
	     return loggingHandler;
	}
	
}
