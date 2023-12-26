package com.apollo.microservice.authentication.services;

import com.apollo.microservice.authentication.clients.ServiceClient;
import com.apollo.microservice.authentication.dtos.UserDTO;
import com.apollo.microservice.authentication.repositories.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Autowired
    private ServiceClient serviceClient;

    public Page<UserDTO> getPageableUsers(Pageable pageable, String token) {
        return userCredentialsRepository.findAll(pageable)
                .map(userCredentials -> {
                    var service = serviceClient.getServiceByOwner(userCredentials.getEmail(), token);
                    return new UserDTO(userCredentials.getEmail(), service.id(), userCredentials.getAuthorities().stream().toList());
                });
    }
}
