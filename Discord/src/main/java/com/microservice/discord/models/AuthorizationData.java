package com.microservice.discord.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationData {

    private String accessToken;

    private String discordId;

    private String categoryId;

    private String authorizationChatId;

    private String name;
}
