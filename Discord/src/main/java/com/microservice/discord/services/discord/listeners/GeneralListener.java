package com.microservice.discord.services.discord.listeners;

import com.microservice.discord.services.discord.listeners.buttonlisteners.BaseButtonListener;
import com.microservice.discord.services.discord.listeners.buttonlisteners.impl.CancelProductPaymentImpl;
import com.microservice.discord.services.discord.listeners.buttonlisteners.impl.GenerateProductPaymentImpl;
import com.microservice.discord.services.discord.listeners.menulisteners.BaseMenuListener;
import com.microservice.discord.services.discord.listeners.menulisteners.impl.SelectProductMenuImpl;
import com.microservice.discord.services.discord.listeners.modallisteners.BaseModalListener;
import com.microservice.discord.services.discord.listeners.modallisteners.impl.AuthorizeModalImpl;
import com.microservice.discord.services.discord.listeners.modallisteners.impl.PaymentModalImpl;
import com.microservice.discord.services.discord.listeners.slashcommands.BaseSlashCommand;
import com.microservice.discord.services.discord.listeners.slashcommands.impl.AuthorizeSlashCommandImpl;
import com.microservice.discord.services.discord.listeners.slashcommands.impl.ProductMenuSlashCommandImpl;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class GeneralListener extends ListenerAdapter {

    private final List<BaseListener<?>> baseListeners;

    @Autowired
    public GeneralListener(
            AuthorizeSlashCommandImpl authorizeSlashCommand,
            ProductMenuSlashCommandImpl productMenuSlashCommand,
            AuthorizeModalImpl authorizeModal,
            PaymentModalImpl paymentModal,
            SelectProductMenuImpl selectProductMenu,
            GenerateProductPaymentImpl generateProductPayment,
            CancelProductPaymentImpl cancelProductPayment
    ) {
        baseListeners = Arrays.asList(
                authorizeSlashCommand,
                productMenuSlashCommand,

                authorizeModal,
                paymentModal,

                selectProductMenu,

                generateProductPayment,
                cancelProductPayment
        );
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        baseListeners
                .stream()
                .filter(command -> command instanceof BaseSlashCommand)
                .map(command -> (BaseSlashCommand) command)
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
    public void onButtonInteraction(ButtonInteractionEvent event) {
        baseListeners.stream()
                .filter(button -> button instanceof BaseButtonListener)
                .map(button -> (BaseButtonListener) button)
                .filter(button -> event.getButton().getId().startsWith(button.getId()))
                .findFirst()
                .ifPresentOrElse(button -> {
                    if (button.getPermission() != null) {
                        if (!event.getMember().hasPermission(button.getPermission())) {
                            // mensagem de erro sem permissao
                            return;
                        }
                    }
                    button.execute(event);
                }, () -> {
                    // retorne mensagem de erro
                });
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        baseListeners.stream()
                .filter(menu -> menu instanceof BaseMenuListener)
                .map(menu -> (BaseMenuListener) menu)
                .filter(menu -> menu.getId().equals(event.getSelectMenu().getId()))
                .findFirst()
                .ifPresentOrElse(menu -> {
                    if (menu.getPermission() != null) {
                        if (!event.getMember().hasPermission(menu.getPermission())) {
                            // mensagem de erro sem permissao
                            return;
                        }
                    }
                    menu.execute(event);
                }, () -> {
                    // retorne mensagem de erro
                });
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
                    if (modal.getPermission() != null) {
                        if (!event.getMember().hasPermission(modal.getPermission())) {
                            // mensagem de erro sem permissao
                            return;
                        }
                    }
                    modal.execute(event);
                }, () -> {
                    // retorne mensagem de erro
                });
    }
}