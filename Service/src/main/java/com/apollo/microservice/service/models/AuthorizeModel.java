package com.apollo.microservice.service.models;

import com.apollo.microservice.service.dtos.AuthorizeDTO;
import com.apollo.microservice.service.enums.AuthStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Calendar;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "service_auth")
public class AuthorizeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String serviceId;

    @Column
    private String serviceKey;

    @Column
    private String discordId;

    @Column
    private String categoryId;

    @Column
    private String chatId;

    @Column
    private String authorizeUrl;

    @Column
    private String accessToken;

    @Column
    private AuthStatus authStatus;

    @Column
    private Calendar expirateAt;

    public AuthorizeModel(AuthorizeDTO authorizeDTO) {
        BeanUtils.copyProperties(authorizeDTO, this);
    }
}