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

    @Column
    private String serviceKey;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    private List<ProductModel> products;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    private List<CouponModel> coupons;

    @Embedded
    private AuthorizationData authorizationData;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;
}