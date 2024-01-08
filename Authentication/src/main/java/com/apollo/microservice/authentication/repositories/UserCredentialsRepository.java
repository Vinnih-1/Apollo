package com.apollo.microservice.authentication.repositories;

import com.apollo.microservice.authentication.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {

    Optional<UserCredentials> findByEmail(String email);

    boolean existsByEmail(String email);
}