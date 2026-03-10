package com.example.inventory_worker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.inventory_worker.dto.OrderRequestDTO;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class InventoryWorker {

	@Value("${aws.sqs.inventory-queue-url}")
	private String queueUrl;

	private final SqsClient sqsClient;

	private final ObjectMapper mapper;

	@PostConstruct
	public void start() {
		new Thread(this::pollMessages).start();
	}

	private void pollMessages() {

		while (true) {
			ReceiveMessageRequest request = ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(5)
					.waitTimeSeconds(10).build();

			List<Message> messages = sqsClient.receiveMessage(request).messages();

			for (Message message : messages) {
				System.out.println("Received message: " + message.body());
				OrderRequestDTO order = jsonToDto(message);
				processMessage(order);
				deleteMessage(message);
			}
		}
	}

	private OrderRequestDTO jsonToDto(Message message) {
		try {
			JsonNode root = mapper.readTree(message.body());

			if (root.has("Message")) {
				String payload = root.get("Message").asText();
				return mapper.readValue(payload, OrderRequestDTO.class);
			}

			return mapper.readValue(message.body(), OrderRequestDTO.class);

		} catch (Exception e) {
			throw new RuntimeException("Error to convert JSON to DTO: " + e.getMessage(), e);
		}
	}

	private void processMessage(OrderRequestDTO order) {
		System.out.println("Updating inventory for order: " + order);
	}

	private void deleteMessage(Message message) {
		DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder().queueUrl(queueUrl)
				.receiptHandle(message.receiptHandle()).build();

		sqsClient.deleteMessage(deleteRequest);
	}
}
