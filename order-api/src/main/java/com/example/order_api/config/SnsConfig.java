package com.example.order_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Configuration
public class SnsConfig {

	@Bean
	public SnsClient sqsClient() {
		return SnsClient.builder().region(Region.SA_EAST_1).build();
	}
}
