package com.microservice.discord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DiscordApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscordApplication.class, args);
	}

}
