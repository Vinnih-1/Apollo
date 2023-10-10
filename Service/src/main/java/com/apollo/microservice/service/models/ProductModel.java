package com.apollo.microservice.service.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Calendar;

@Data
@Builder
@Entity
@Table(name = "products_service")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private double price;

    @Column
    private String serviceId;

    @Column
    private String description;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;
}