package com.microservice.discord.requests;

import com.google.gson.Gson;
import com.microservice.discord.dtos.AuthorizeDTO;
import com.microservice.discord.dtos.PaymentDTO;
import com.microservice.discord.dtos.ServiceDTO;
import com.microservice.discord.models.AuthorizeModel;
import com.microservice.discord.models.PaymentModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

public class ServiceRequest {

    private static ServiceRequest instance =  null;

    private static final String BEARER_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2aW5pY2l1c2FsYjEwQGdtYWlsLmNvbSIsImlzcyI6InVzZXIiLCJpZCI6ImRlNzBlM2ZlLWRmODgtNGU1MC04MjViLTNlNzIyNmI5OTZhNyIsImV4cCI6MTcwNTgxODgwNywiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl19.b8K6TUzpW2kbA9J9c3BV8nGOIaocNZh1eKl49Ww_28g";

    private static final String URL = "http://192.168.1.106:8080";

    private final HttpClient client;

    private ServiceRequest() {
        client = HttpClient.newBuilder()
                .build();
    }

    public void authorizeRequest(AuthorizeDTO authorizeDTO, Consumer<AuthorizeModel> onSuccess, Consumer<Void> onFailure) {
        var gson = new Gson();
        var request = client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(URL + String.format(
                                "/service/authorize?serviceId=%s&serviceKey=%s&discordId=%s&categoryId=%s&chatId=%s",
                                authorizeDTO.serviceId(), authorizeDTO.serviceKey(), authorizeDTO.discordId(), authorizeDTO.categoryId(), authorizeDTO.chatId()
                        )))
                .build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() >= 400) {
                        onFailure.accept(null);
                        return;
                    }
                    var authorizeModel = gson.fromJson(response.body(), AuthorizeModel.class);
                    onSuccess.accept(authorizeModel);
                });

        request.join();
    }

    public void sendPaymentRequest(PaymentDTO paymentDTO, Consumer<PaymentModel> onSuccess) {
        var gson = new Gson();
        var request = client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(URL + String.format(
                                "/service/product/%s/payment?payer=%s&chatId=%s",
                                paymentDTO.productId(), paymentDTO.payer(), paymentDTO.chatId()
                        )))
                        .header("Authorization", BEARER_TOKEN)
                        .build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    var paymentModel = gson.fromJson(response.body(), PaymentModel.class);
                    onSuccess.accept(paymentModel);
                });

        request.join();
    }

    public void retrieveServiceByDiscordId(String discordId, Consumer<ServiceDTO> onSuccess) {
        var gson = new Gson();
        var request = client.sendAsync(HttpRequest.newBuilder()
                .uri(URI.create(URL + "/service/discord/" + discordId))
                .header("Authorization", BEARER_TOKEN)
                .build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    var serviceDTO = gson.fromJson(response.body(), ServiceDTO.class);
                    onSuccess.accept(serviceDTO);
                });

        request.join();
    }

    public void retrieveServiceByServiceId(String serviceId, Consumer<ServiceDTO> onSuccess) {
        var gson = new Gson();
        var request = client.sendAsync(HttpRequest.newBuilder()
                        .uri(URI.create(URL + "/service/" + serviceId))
                        .header("Authorization", BEARER_TOKEN)
                        .build(), HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    var serviceDTO = gson.fromJson(response.body(), ServiceDTO.class);
                    onSuccess.accept(serviceDTO);
                });

        request.join();
    }

    public static ServiceRequest getInstance() {
        if (instance == null) {
            instance = new ServiceRequest();
        }
        return instance;
    }
}