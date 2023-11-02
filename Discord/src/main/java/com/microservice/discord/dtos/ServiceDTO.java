package com.microservice.discord.dtos;

import java.util.List;

public record ServiceDTO(
        String serviceId,
        String owner,
        String discordId,
        String categoryId,
        String serviceType,
        List<ProductDTO> products
) {
}
