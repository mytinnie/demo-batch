package com.tqn.demo.configuration;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.boeing.ai.repository")
@EntityScan(basePackages = "com.boeing.ai.model")
public class RepositoryConfiguration {
}
