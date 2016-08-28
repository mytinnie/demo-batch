package com.tqn.demo.batch;

//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan("com.boeing.ai.controller")
public class App {
	public static void main(String[] args) throws Exception {
		// System.exit is common for Batch applications since the exit code can be used to drive a workflow
		//System.exit(SpringApplication.exit(SpringApplication.run(App.class, args)));
		SpringApplication.run(App.class, args);
	}
}
