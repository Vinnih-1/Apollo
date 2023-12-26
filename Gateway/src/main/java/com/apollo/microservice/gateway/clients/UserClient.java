package com.apollo.microservice.gateway.clients;

import com.apollo.microservice.gateway.dtos.GatewayAuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AUTHENTICATION", url = "http://DESKTOP-JVA7972:8085")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/validate")
    GatewayAuthenticationDTO validateToken(@RequestParam("token") String token);
}
