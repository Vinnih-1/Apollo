package com.apollo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ApolloEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApolloEurekaApplication.class, args);
	}

}
