package com.apollo.microservice.authentication.services;

import com.apollo.microservice.authentication.repositories.UserCredentialsRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authozationHeader = request.getHeader("Authorization");

        if (authozationHeader != null && authozationHeader.startsWith("Bearer ")) {
            var token = authozationHeader.substring(7);
            var email = tokenService.getEmailSubject(token);
            var userCredential = userCredentialsRepository.findByEmail(email).orElse(null);

            if (userCredential == null) {
                filterChain.doFilter(request, response);
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(userCredential, null, userCredential.getAuthorities())
            );
        }

        filterChain.doFilter(request, response);
    }
}
