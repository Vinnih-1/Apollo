package com.microservice.discord.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.discord.consumers.events.AuthExpiredEvent;
import com.microservice.discord.consumers.events.AuthPendingEvent;
import com.microservice.discord.consumers.events.AuthSuccessEvent;
import com.microservice.discord.consumers.events.BaseAuthEvent;
import com.microservice.discord.models.AuthorizeModel;
import com.microservice.discord.services.discord.DiscordService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthorizeServiceConsumer {

    @Autowired
    private DiscordService discordService;

    private final List<BaseAuthEvent> events = Arrays.asList(
            new AuthExpiredEvent(),
            new AuthPendingEvent(),
            new AuthSuccessEvent()
    );

    @RabbitListener(queues =  "${broker.queue.auth.service}")
    public void listenAuthorizeServiceQueue(@Payload AuthorizeModel authorizeModel) {
        var guild = discordService.getJda().getGuildById(authorizeModel.getDiscordId().split("_")[0]);
        var channel = guild.getTextChannelById(authorizeModel.getDiscordId().split("_")[1]);

        events.stream()
                .filter(event -> event.getAuthStatus() == authorizeModel.getAuthStatus())
                .findFirst()
                .ifPresent(event -> event.execute(guild, channel, authorizeModel));
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}