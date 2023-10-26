package com.microservice.discord.services.discord.listeners;

import com.microservice.discord.producers.AuthorizeServiceProducer;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import com.microservice.discord.services.discord.listeners.modallisteners.impl.AuthorizeModalListener;
import com.microservice.discord.services.discord.listeners.slashcommands.BaseSlashCommand;
import com.microservice.discord.services.discord.listeners.slashcommands.impl.AuthorizeSlashCommand;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GeneralListener extends ListenerAdapter {

    private final List<BaseListener<?>> baseListeners;

    public GeneralListener(AuthorizeServiceProducer authorizeServiceProducer) {
        this.baseListeners = Arrays.asList(
                new AuthorizeSlashCommand(),

                new AuthorizeModalListener(authorizeServiceProducer)
        );
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        baseListeners
                .stream()
                .filter(command -> command instanceof BaseSlashCommand)
                .map(command -> (BaseSlashCommand)command)
                .forEach(command -> event.getGuild().upsertCommand(command.getName(), command.getDescription()).queue());
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        baseListeners
                .stream()
                .filter(cmd -> cmd instanceof BaseSlashCommand)
                .map(cmd -> (BaseSlashCommand) cmd)
                .forEach(cmd -> event.getGuild().upsertCommand(cmd.getName(), cmd.getDescription()).queue());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        var commandName = event.getCommandString().replace("/", "");

        var command = (BaseSlashCommand) baseListeners
                .stream()
                .filter(cmd -> cmd instanceof BaseSlashCommand)
                .filter(cmd -> ((BaseSlashCommand) cmd).getName().equals(commandName))
                .findFirst()
                .orElse(null);

        if (command == null) return;
        if (command.getPermission() == null) return;
        if (!event.getMember().hasPermission(command.getPermission())) return;

        command.execute(event);
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        var identifier = event.getModalId().split("_")[0];

        baseListeners.stream()
                .filter(modal -> modal instanceof BaseModalListener)
                .map(modal -> (BaseModalListener) modal)
                .filter(modal -> modal.getId().startsWith(identifier))
                .findFirst()
                .ifPresentOrElse(modal -> {
                    if (!event.getMember().hasPermission(modal.getPermission())) {
                        // mensagem de erro sem permissao
                        return;
                    }
                    modal.execute(event);
                }, () -> {
                    // retorne mensagem de erro
                });
    }
}