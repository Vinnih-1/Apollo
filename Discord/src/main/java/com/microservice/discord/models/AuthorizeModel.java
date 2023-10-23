package com.microservice.discord.models;

import com.microservice.discord.dtos.AuthorizeDTO;
import com.microservice.discord.enums.AuthStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Calendar;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeModel {

    private String id;

    private String serviceId;

    private String serviceKey;

    private String discordId;

    private String authorizeUrl;

    private String accessToken;

    private AuthStatus authStatus;

    private Calendar expirateAt;

    public AuthorizeModel(AuthorizeDTO authorizeDTO) {
        BeanUtils.copyProperties(authorizeDTO, this);
    }
}