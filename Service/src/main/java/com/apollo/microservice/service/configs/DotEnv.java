package com.apollo.microservice.service.configs;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotEnv {

    private final Dotenv dotenv;

    public DotEnv() {
        this.dotenv = Dotenv.configure()
                .directory("../Service/.env")
                .load();
    }

    public String get(String key) {
        return dotenv.get(key);
    }
}
