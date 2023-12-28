package com.apollo.microservice.service.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_services")
public class ServiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String owner;

    @Column(unique = true)
    private String serviceKey;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    private List<ProductModel> products;

    @Column
    private String discordId;

    @Column
    private String categoryId;

    @Column
    private String accessToken;

    @Column
    private boolean isSuspended;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Temporal(TemporalType.DATE)
    private Calendar expirateAt;
}