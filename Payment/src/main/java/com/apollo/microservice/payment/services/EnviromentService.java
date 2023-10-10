package com.apollo.microservice.payment.services;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnviromentService {

    private static EnviromentService instance = null;
    private static Dotenv dotenv = null;

    private EnviromentService() {
        EnviromentService.dotenv = Dotenv.load();
    }

    public String getEnv(String key) {
        return dotenv.get(key);
    }

    public static EnviromentService getInstance() {
        if (instance == null) {
            instance = new EnviromentService();
            return instance;
        }
        return instance;
    }
}
