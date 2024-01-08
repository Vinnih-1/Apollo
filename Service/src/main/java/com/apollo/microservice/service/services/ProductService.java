package com.apollo.microservice.service.services;

import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductModel findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public ProductModel createNewProduct(ProductDTO productDTO, ServiceModel serviceModel) {
        var product = ProductModel.builder()
                .name(productDTO.getName().toUpperCase())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .service(serviceModel)
                .createAt(Calendar.getInstance())
                .build();

        productRepository.saveAndFlush(product);
        return product;
    }

    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }
}
