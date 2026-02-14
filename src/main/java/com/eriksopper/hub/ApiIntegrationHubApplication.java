package com.eriksopper.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ApiIntegrationHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiIntegrationHubApplication.class, args);
	}

}
