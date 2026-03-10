package com.example.inventory_worker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

	@Bean
	public SqsClient sqsClient() {
		return SqsClient.builder().region(Region.SA_EAST_1).build();
	}
}
