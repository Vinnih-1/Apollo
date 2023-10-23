package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.AuthorizeModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizeRepository extends JpaRepository<AuthorizeModel, String> {

    Optional<AuthorizeModel> findByServiceId(String serviceId);
}
