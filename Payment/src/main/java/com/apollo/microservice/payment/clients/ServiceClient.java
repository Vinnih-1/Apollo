package com.apollo.microservice.payment.clients;

import com.apollo.microservice.payment.dtos.ServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SERVICE", url = "DESKTOP-JVA7972:8080")
public interface ServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/service/email/")
    ServiceDTO getServiceByOwner(@RequestParam("owner") String owner, @RequestHeader("Authorization") String token);

    @RequestMapping(method = RequestMethod.GET, value = "/service/")
    ServiceDTO getServiceByToken(@RequestHeader("Authorization") String token);
}
