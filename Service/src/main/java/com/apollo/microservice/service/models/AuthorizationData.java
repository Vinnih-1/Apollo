package com.apollo.microservice.service.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationData {

    @JsonIgnore
    private String accessToken;

    private String discordId;

    private String categoryId;

    private String authorizationChatId;

    private String name;
}
