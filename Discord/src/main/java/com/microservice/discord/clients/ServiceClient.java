package com.microservice.discord.clients;

import com.microservice.discord.models.ServiceModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Service", url = "DESKTOP-JVA7972:8080")
public interface ServiceClient {

    @GetMapping("/service/authorize")
    String authorizationRequest(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("discordId") String discordId,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("chatId") String chatId
    );

    @GetMapping("/service/discord")
    ServiceModel findServiceByDiscordId(@RequestParam("id") String id, @RequestHeader("Authorization") String token);
}
