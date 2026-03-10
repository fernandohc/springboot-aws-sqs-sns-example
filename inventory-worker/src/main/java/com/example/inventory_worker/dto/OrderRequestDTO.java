package com.example.inventory_worker.dto;

public record OrderRequestDTO(String orderId, String customerEmail, String productId, int quantity) {
}
