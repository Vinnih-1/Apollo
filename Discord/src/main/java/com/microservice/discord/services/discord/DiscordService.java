package com.microservice.discord.services.discord;

import com.microservice.discord.producers.AuthorizeServiceProducer;
import com.microservice.discord.services.discord.listeners.GeneralListener;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Service
public class DiscordService {

    private final JDA jda;

    @Autowired
    public DiscordService(AuthorizeServiceProducer authorizeServiceProducer) throws InterruptedException {
        var dotenv = Dotenv.configure()
                .directory("../Discord/.env")
                .load();

        this.jda = JDABuilder
                .createDefault(dotenv.get("APOLLO_PRODUCTION_TOKEN_BOT"))
                .addEventListeners(new GeneralListener(authorizeServiceProducer))
                .build()
                .awaitReady();

        jda.getPresence().setActivity(Activity.watching(jda.getGuilds().size() + " serviços ativos..."));
    }
}