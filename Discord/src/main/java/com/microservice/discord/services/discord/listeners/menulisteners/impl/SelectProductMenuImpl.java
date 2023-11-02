package com.microservice.discord.services.discord.listeners.menulisteners.impl;

import com.microservice.discord.messages.ProductMessages;
import com.microservice.discord.requests.ServiceRequest;
import com.microservice.discord.services.discord.listeners.menulisteners.BaseMenuListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class SelectProductMenuImpl extends BaseMenuListener {

    public SelectProductMenuImpl() {
        super(
                "menu.products",
                "products",
                "Armazena os produtos do serviço num menu",
                null
        );
    }

    @Override
    public void execute(StringSelectInteractionEvent event) {
        event.editSelectMenu(event.getSelectMenu().createCopy()
                .setDefaultValues(new ArrayList<>()).build()).queue();

        var values = event.getInteraction().getSelectedOptions().get(0).getValue();
        var productId = values.split("_")[0];

        ServiceRequest.getInstance()
                .retrieveServiceByDiscordId(event.getGuild().getId(), service -> {
                    var category = service.categoryId();

                    if (event.getGuild().getCategoryById(category) == null) {
                        event.getChannel().asTextChannel().sendMessageEmbeds(ProductMessages.CATEGORY_NOT_FOUND().build()).queue();
                        return;
                    }

                    event.getGuild().getCategoryById(service.categoryId())
                            .createTextChannel(event.getUser().getEffectiveName())
                            .addPermissionOverride(event.getMember(),
                                    Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES),
                                    null)
                            .queue(channel -> {
                                var product = service.products().stream()
                                                .filter(selected -> Long.parseLong(productId) == selected.id())
                                                        .findFirst().orElse(null);

                                if (product == null) {
                                    channel.sendMessageEmbeds(ProductMessages.PRODUCT_NOT_FOUND().build()).queue();
                                    return;
                                }

                                channel.sendMessageEmbeds(ProductMessages.CREATING_PRODUCT_PAYMENT(product).build())
                                        .addActionRow(Button.success("generate_" + values, "Pagar").withEmoji(Emoji.fromUnicode("U+1F4B3")),
                                                Button.danger("cancel_" + values, "Cancelar").withEmoji(Emoji.fromUnicode("U+1F4EC")))
                                        .queue();
                            });
                });
    }
}
