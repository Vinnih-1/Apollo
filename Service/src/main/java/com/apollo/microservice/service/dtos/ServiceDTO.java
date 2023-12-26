package com.apollo.microservice.service.dtos;

import com.apollo.microservice.service.enums.ServiceType;
import com.apollo.microservice.service.models.ProductModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {

        private String id;

        private String serviceKey;

        @NotNull @NotBlank private String owner;

        private String discordId;

        private String categoryId;

        @NotNull private  ServiceType serviceType;

        private long buyers;

        private double moneyMoved;

        private List<ProductDTO> products;
}
