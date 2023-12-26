package com.apollo.microservice.payment.clients;

import com.apollo.microservice.payment.dtos.GatewayAuthenticationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AUTHENTICATION", url = "DESKTOP-JVA7972:8080")
public interface UserClient {

    @RequestMapping(method = RequestMethod.GET, value = "/auth/user")
    GatewayAuthenticationDTO userByToken(@RequestHeader("Authorization") String token);
}
