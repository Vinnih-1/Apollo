package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
}
