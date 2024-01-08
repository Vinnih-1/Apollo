package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.enums.AuthStatus;

public record AuthorizeDTO(
        String id,
        String serviceId,
        String serviceKey,
        String discordId,
        String authorizeUrl,
        String accessToken,
        AuthStatus authStatus
) {
}