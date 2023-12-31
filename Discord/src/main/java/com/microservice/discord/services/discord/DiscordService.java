package com.microservice.discord.services.discord;

import com.microservice.discord.configs.DotEnv;
import com.microservice.discord.services.discord.listeners.GeneralListener;
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

    private final GeneralListener generalListener;

    @Autowired
    public DiscordService(GeneralListener generalListener, DotEnv dotEnv) throws InterruptedException {
        this.generalListener = generalListener;

        this.jda = JDABuilder
                .createDefault(dotEnv.get("APOLLO_PRODUCTION_TOKEN_BOT"))
                .addEventListeners(generalListener)
                .build()
                .awaitReady();

        jda.getPresence().setActivity(Activity.watching(jda.getGuilds().size() + " serviços ativos..."));
    }
}