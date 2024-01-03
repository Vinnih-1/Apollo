package com.apollo.microservice.authentication.controllers;

import com.apollo.microservice.authentication.dtos.AuthenticationDTO;
import com.apollo.microservice.authentication.dtos.GatewayAuthenticationDTO;
import com.apollo.microservice.authentication.dtos.UserDTO;
import com.apollo.microservice.authentication.services.AuthenticationService;
import com.apollo.microservice.authentication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth/")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginAccount(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var token = authenticationService.authenticateUserCredentials(authenticationDTO);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/validate")
    public ResponseEntity<GatewayAuthenticationDTO> testToken(@RequestParam("token") String token) {
        var gatewayAuthenticationDTO = authenticationService.isValidToken(token.startsWith("Bearer ") ? token.substring(7) : token);
        return ResponseEntity.ok(gatewayAuthenticationDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<GatewayAuthenticationDTO> getUserAccount(@RequestHeader("Authorization") String token) {
        var gatewayAuthenticationDTO = authenticationService.isValidToken(token.startsWith("Bearer ") ? token.substring(7) : token);
        return ResponseEntity.ok(gatewayAuthenticationDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getUsersAccount(@RequestHeader("Authorization") String token) {
        var gatewayAuthenticationDTO = authenticationService.isValidToken(token.startsWith("Bearer ") ? token.substring(7) : token);
        if (!gatewayAuthenticationDTO.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userService.getPageableUsers(PageRequest.of(0, 20), token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDTO> registerAccount(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var userCredentials = authenticationService.registerUserCredentials(authenticationDTO);
        if (userCredentials == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(authenticationDTO);
    }
}