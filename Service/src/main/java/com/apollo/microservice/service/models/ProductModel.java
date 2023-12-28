package com.apollo.microservice.service.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_products")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private double price;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceModel service;

    @Column(length = 50)
    private String description;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;
}