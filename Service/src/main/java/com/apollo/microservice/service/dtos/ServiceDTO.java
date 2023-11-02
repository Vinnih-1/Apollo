package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.enums.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ServiceDTO(
        String serviceId,
        @NotNull @NotBlank String owner,
        String discordId,
        String categoryId,
        @NotNull ServiceType serviceType,
        List<ProductDTO> products
        ) {
}
