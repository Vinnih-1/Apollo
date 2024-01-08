package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    Optional<List<ProductModel>> findProductsByServiceId(String serviceId);
}
