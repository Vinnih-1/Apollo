package com.apollo.microservice.authentication.services;

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

    public Page<UserDTO> getPageableUsers(Pageable pageable, String token) {
        return userCredentialsRepository.findAll(pageable)
                .map(user -> new UserDTO(user.getEmail(), user.getAuthorities().stream().toList()));
    }
}
