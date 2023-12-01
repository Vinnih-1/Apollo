package com.apollo.microservice.authentication.services;

import com.apollo.microservice.authentication.dtos.AuthenticationDTO;
import com.apollo.microservice.authentication.dtos.GatewayAuthenticationDTO;
import com.apollo.microservice.authentication.entities.UserCredentials;
import com.apollo.microservice.authentication.repositories.UserCredentialsRepository;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialsRepository.findByEmail(username).orElse(null);
    }

    public String authenticateUserCredentials(AuthenticationDTO authenticationDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationDTO.email(), authenticationDTO.password()
        );
        var authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenService.generateToken((UserCredentials) authenticate.getPrincipal());
    }

    public GatewayAuthenticationDTO isValidToken(String token) {
        var gatewayAuthenticationDTO = new GatewayAuthenticationDTO();
        gatewayAuthenticationDTO.setToken(token);

        try {
            tokenService.getSubject(token);
            gatewayAuthenticationDTO.setValid(true);
        } catch (TokenExpiredException e) {
            gatewayAuthenticationDTO.setValid(false);
            System.out.println("expirado");
        }
        return gatewayAuthenticationDTO;
    }

    public UserCredentials registerUserCredentials(AuthenticationDTO authenticationDTO) {
        if (userCredentialsRepository.existsByEmail(authenticationDTO.email()))
            return null;

        var encryptedPassword = new BCryptPasswordEncoder().encode(authenticationDTO.password());
        var userCredentials = UserCredentials.builder()
                .email(authenticationDTO.email())
                .password(encryptedPassword)
                .build();

        userCredentialsRepository.save(userCredentials);
        return userCredentials;
    }
}