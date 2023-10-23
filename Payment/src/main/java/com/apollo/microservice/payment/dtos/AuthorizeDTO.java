package com.apollo.microservice.payment.dtos;

import com.apollo.microservice.payment.enums.AuthStatus;

public record AuthorizeDTO(
        String serviceId,
        String serviceKey,
        String discordId,
        String authorizeUrl,
        String accessToken,
        AuthStatus authStatus
) {
}