package com.apollo.microservice.service.repositories;

import com.apollo.microservice.service.models.ServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceModel, String> {

    Optional<ServiceModel> findByOwner(String owner);

    @Query("SELECT s FROM ServiceModel s WHERE s.authorizationData.discordId = :discordId")
    Optional<ServiceModel> findByDiscordId(String discordId);
}