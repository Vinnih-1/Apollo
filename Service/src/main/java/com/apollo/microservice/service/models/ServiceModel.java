package com.apollo.microservice.service.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "services")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String owner;

    @Column(unique = true)
    private String serviceKey;

    @Column
    private String discordId;

    @Column
    private String accessToken;

    @Column
    private boolean isSuspended;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Temporal(TemporalType.DATE)
    private Calendar expirateAt;
}