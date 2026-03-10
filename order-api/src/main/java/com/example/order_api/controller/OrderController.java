package com.example.order_api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_api.dto.OrderRequestDTO;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final SnsClient snsClient;
	private final ObjectMapper mapper;

	@Value("${aws.sns.order-topic-arn}")
	private String topicArn;

	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody OrderRequestDTO order) {

		String orderJson = mapper.writeValueAsString(order);

		PublishRequest request = PublishRequest.builder().topicArn(topicArn).message(orderJson).build();

		snsClient.publish(request);

		return ResponseEntity.ok().build();
	}
}
