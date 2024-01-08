package com.apollo.microservice.service.services;

import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.ServiceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PlanService {

    @Autowired
    private ServiceRepository serviceRepository;

    public Page<ServiceModel> getPageableServices(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    public ServiceModel createNewService(String owner) {
        var service = ServiceModel.builder()
                .owner(owner)
                .serviceKey(RandomStringUtils.randomAlphanumeric(8))
                .createAt(Calendar.getInstance())
                .build();
        serviceRepository.saveAndFlush(service);
        return service;
    }

    public ServiceModel findByDiscordId(String discordId) {
        return serviceRepository.findByDiscordId(discordId).orElse(null);
    }

    public ServiceModel findServiceById(String id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public ServiceModel findByOwner(String owner) {
        return serviceRepository.findByOwner(owner).orElse(null);
    }

    public void savePlanService(ServiceModel serviceModel) {
        serviceRepository.saveAndFlush(serviceModel);
    }
}
