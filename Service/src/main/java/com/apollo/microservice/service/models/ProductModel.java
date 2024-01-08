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
@Table(name = "tb_products")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String name;

    @Column
    private double price;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<PaymentModel> payments;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceModel service;

    @Column(length = 50)
    private String description;

    @Temporal(TemporalType.DATE)
    private Calendar createAt;

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}