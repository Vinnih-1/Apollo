package com.apollo.microservice.gateway.filters;

import com.apollo.microservice.gateway.clients.UserClient;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {

    @Autowired
    private UserClient userClient;

    private static final List<String> allowedEndpoints = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/auth/validate",
            "/service/authorize",
            "/service/validate"
    );

    public AuthorizationFilter() {
        super(NameConfig.class);
    }

    @Override
    public GatewayFilter apply(NameConfig config) {
        return ((exchange, chain) -> {
            if (allowedEndpoints.contains(exchange.getRequest().getPath().value()))
                return chain.filter(exchange);
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                throw new RuntimeException();
            var authHeader = exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader == null || !authHeader.startsWith("Bearer"))
                throw new RuntimeException();
            var user = userClient.validateToken(authHeader.substring(7));
            if (!user.valid()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        });
    }
}
