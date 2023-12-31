package com.microservice.discord.messages;

import com.microservice.discord.dtos.ProductDTO;
import com.microservice.discord.models.PaymentModel;
import com.microservice.discord.models.ServiceModel;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class ProductMessages {

    private static final String SITE_URL = "https://loja.apollodiscord.com";

    private static final String LOGO_URL = "https://cdn.discordapp.com/attachments/1148878336651698208/1152991830774906920/Apollo-512x512.png";

    public static EmbedBuilder PRODUCT_MENU_EMBED(ServiceModel serviceModel) {
        var embed = getEmbedBuilder("Menu de Produtos");

        embed.setFooter("Produtos・Clique em um produto para adquirir",
                LOGO_URL);

        embed.addField(":dollar: **__Produtos disponíveis__**", String.valueOf(serviceModel.getProducts().size()), true);
        embed.addField(":identification_card: **__Cupons ativos__**", String.valueOf(serviceModel.getCoupons().size()), true);

        embed.setDescription(String.format("Para adquirir um produto, basta selecioná-los clicando no" +
                " que desejar para gerar o QRCode de pagamento. Após isso, basta" +
                " apontar a câmera do seu celular e pronto!  \n \n" +
                "Para adquirir um serviço igual, basta [clicar aqui](%s) \n⠀", SITE_URL));

        return embed;
    }

    public static EmbedBuilder PRODUCT_PAYED_EMBED(PaymentModel paymentModel) {
        var embed = getEmbedBuilder("Produto Pago");

        embed.addField("Referência externa", paymentModel.getExternalReference(), false);
        embed.setDescription("Detectamos o seu pagamento pelo produto.");
        embed.setThumbnail(LOGO_URL);

        return embed;
    }

    public static EmbedBuilder PAYMENT_PENDING_EMBED(PaymentModel paymentModel) {
        var embed = getEmbedBuilder("Pagamento em Pendência");

        embed.setFooter("Pendente・Basta apontar o celular para o QRCode",
                LOGO_URL);

        embed.addField(":file_folder: **__Compra gerada Por__**",
                paymentModel.getPayer(),
                false);
        embed.addField(":dollar: **__Preço__**",
                "R$ " + String.valueOf(paymentModel.getProduct().getPrice()).replace(".", ","),
                false);

        embed.setDescription("Este QRCode tem validade de 30 minuto(s).");

        return embed;
    }

    public static EmbedBuilder CREATING_PRODUCT_PAYMENT(ProductDTO product) {
        var embed = getEmbedBuilder("Criando o QRCode");

        embed.setFooter("Processando・Clique em pagar para processar o pedido",
                LOGO_URL);

        embed.addField(":file_folder: **__Produto__**",
                product.getName(),
                false);
        embed.addField(":dollar: **__Preço__**",
                "R$ " + String.valueOf(product.getPrice()).replace(".", ","),
                false);

        embed.setDescription("Estamos gerando o seu QRCode, aguarde um momento, por favor.");

        return embed;
    }

    public static EmbedBuilder CATEGORY_NOT_FOUND() {
        var embed = getEmbedBuilder("Categoria não Encontrada");

        embed.setFooter("Falha・Siga as instruções acima");

        embed.addField(":mag_right: **__Causa do problema__**",
                "Aparentemente a categoria de criação de pagamentos foi deletada.",
                false);

        embed.setDescription(String.format("Para solucionar este problema, utilize o comando **/autorizar**. \n" +
                "Caso você não tenha um serviço ativo, você pode " +
                "adquirir um [clicando aqui](%s) \n⠀", SITE_URL));

        return embed;
    }

    public static EmbedBuilder PRODUCT_NOT_FOUND() {
        var embed = getEmbedBuilder("Produto não Existe");

        embed.setFooter("Falha・Não foi possível criar este QRCode");

        embed.addField(":mag_right: **__Este produto não existe mais__**",
                "Não foi possível localizar este produto no serviço",
                false);

        embed.setDescription(String.format("Não é possível localizar este produto na coleção" +
                " de produtos deste serviço. Para receber a lista atualizada de produtos" +
                " utilize o comando **/produtos**. \n⠀\n" +
                "Você também pode criar novos produtos [clicando aqui](%s)", SITE_URL));

        return embed;
    }

    public static EmbedBuilder EMPTY_PRODUCT_LIST() {
        var embed = getEmbedBuilder("Não há Produtos");
        embed.setFooter("Falha・Nenhum produto encontrado");

        embed.addField(":mag_right: **__Nenhum produto registrado__**",
                "Você ainda não registrou nenhum produto",
                false);

        embed.setDescription(String.format("Não é possível localizar nenhum produto em sua coleção" +
                " de produtos neste serviço. Crie um produto clicando no link abaixo e" +
                " utilize o comando **/produtos**. \n⠀\n" +
                "Você pode criar novos produtos [clicando aqui](%s)", SITE_URL));

        return embed;
    }

    private static EmbedBuilder getEmbedBuilder(String title) {
        var embed = new EmbedBuilder();
        embed.setColor(Color.GRAY);
        embed.setAuthor(title,
                null,
                LOGO_URL);

        embed.setThumbnail(LOGO_URL);
        return embed;
    }
}