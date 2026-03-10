package com.example.order_api.dto;

public record OrderRequestDTO(String orderId, String customerEmail, String productId, int quantity) {
}
