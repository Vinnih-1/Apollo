package com.microservice.discord.dtos;

public record AuthorizeDTO(
        String serviceId,
        String serviceKey,
        String discordId,
        String authorizeUrl,
        String accessToken
) {
}