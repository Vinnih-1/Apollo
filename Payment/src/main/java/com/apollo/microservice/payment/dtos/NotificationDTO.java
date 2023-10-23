package com.apollo.microservice.payment.dtos;

public record NotificationDTO(
    String id,
    boolean liveMode,
    String type,
    String dateCreated,
    String userId,
    String apiVersion,
    String action,
    NotificationDataDTO data
) {
}