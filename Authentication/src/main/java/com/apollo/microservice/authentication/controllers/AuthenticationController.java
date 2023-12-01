package com.apollo.microservice.authentication.controllers;

import com.apollo.microservice.authentication.dtos.AuthenticationDTO;
import com.apollo.microservice.authentication.dtos.GatewayAuthenticationDTO;
import com.apollo.microservice.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public ResponseEntity<String> loginAccount(@RequestBody AuthenticationDTO authenticationDTO) {
        var token = authenticationService.authenticateUserCredentials(authenticationDTO);

        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate")
    public ResponseEntity<GatewayAuthenticationDTO> testToken(@RequestParam("token") String token) {

        var gatewayAuthenticationDTO = authenticationService.isValidToken(token);
        return ResponseEntity.ok(gatewayAuthenticationDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDTO> registerAccount(@RequestBody AuthenticationDTO authenticationDTO) {
        var userCredentials = authenticationService.registerUserCredentials(authenticationDTO);
        if (userCredentials == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        return ResponseEntity.ok(authenticationDTO);
    }
}