package com.tqn.demo.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class MyInfrastructureConfiguration implements InfrastructureConfiguration {
	@Autowired
	private Environment environment;
//	@Autowired
//	private ResourceLoader resourceLoader;
	
//	@PostConstruct
//	protected void initialize() {
//		// initialize BATCH tables
//		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
////		populator.addScript(resourceLoader.getResource(environment.getProperty("batch.drop.script")));
////		populator.addScript(resourceLoader.getResource(environment.getProperty("batch.schema.script")));
//		populator.addScript(resourceLoader.getResource(environment.getProperty("batch.business.schema.script")));
//		populator.setContinueOnError(true);
//		DatabasePopulatorUtils.execute(populator , dataSource());
//	}
	
	@Bean
	public JobRepository jobRepository(DataSource ds) throws Exception {
	    JobRepositoryFactoryBean factory =  new JobRepositoryFactoryBean();
	    factory.setTransactionManager(new DataSourceTransactionManager(ds));
	    factory.setDataSource(ds);
	    factory.setIsolationLevelForCreate("ISOLATION_READ_UNCOMMITTED");
	    return factory.getObject();
	}
	
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

		
//	@Bean
//	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//		return new JdbcTemplate(dataSource);
//	}
	
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getProperty("batch.jdbc.driver"));
		dataSource.setUrl(environment.getProperty("batch.jdbc.url"));
		dataSource.setUsername(environment.getProperty("batch.jdbc.username"));
		dataSource.setPassword(environment.getProperty("batch.jdbc.password"));
		return dataSource;
	}
	
}
