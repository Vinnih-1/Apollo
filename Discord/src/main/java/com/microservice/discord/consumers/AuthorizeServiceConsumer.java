package com.microservice.discord.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.discord.consumers.events.AuthSuccessEvent;
import com.microservice.discord.dtos.ServiceDTO;
import com.microservice.discord.services.discord.DiscordService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeServiceConsumer {

    @Autowired
    private DiscordService discordService;

    /**
     * Recebe a notificação de autorização do serviço
     * para a conta do Mercado Pago do usuário e
     * notifica via mensagem embed diretamente no chat em que
     * o comando /autorizar foi executado.
     *
     * @param serviceDTO
     */
    @RabbitListener(queues = "authorizer.discord")
    public void listenAuthorizeServiceQueue(@Payload ServiceDTO serviceDTO) {
        var guild = discordService.getJda().getGuildById(serviceDTO.getAuthorizationData().getDiscordId());
        var channel = guild.getTextChannelById(serviceDTO.getAuthorizationData().getAuthorizationChatId());

        new AuthSuccessEvent().execute(guild, channel, serviceDTO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        var objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}