package com.microservice.discord.dtos;

public record ProductDTO(
        Long id,
        String name,
        String description,
        double price,
        String serviceId
) {
}
