package com.tqn.demo.configuration;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

public interface InfrastructureConfiguration {
	@Bean
	public abstract DataSource dataSource();
	
}
