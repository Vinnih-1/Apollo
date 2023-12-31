package com.apollo.microservice.service.controllers;

import com.apollo.microservice.service.models.AuthorizationData;
import com.apollo.microservice.service.producers.PaymentAuthorizeProducer;
import com.apollo.microservice.service.services.PaymentService;
import com.apollo.microservice.service.services.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class AuthorizationController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PlanService planService;

    @Autowired
    private PaymentAuthorizeProducer paymentAuthorizeProducer;


    /**
     * Cria o objeto AuthorizationData no ServiceModel e salva todos
     * os dados, como discordId, category e chatId para uso posterior.
     * <p>
     * Endpoint utilizado pelo microsserviço do Discord via slashcommand
     * /autorizar. Gera e retorna o link para a autorização da aplicação
     * do Apollo na conta do usuário do Mercado Pago para obtenção
     * do Access Token.
     *
     * @param serviceId  ID do Serviço do usuário
     * @param serviceKey Senha do Serviço do usuário
     * @param discordId  ID do Discord em que o comando foi executado
     * @param categoryId Categoria em que os chats de pagamento serão criados
     * @param chatId     ID do Chat em que o comando foi executado
     * @return Link de autorização do Mercado Pago
     */
    @GetMapping("/authorize")
    public ResponseEntity<String> authorizationRequest(
            @RequestParam("serviceId") String serviceId,
            @RequestParam("serviceKey") String serviceKey,
            @RequestParam("discordId") String discordId,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("chatId") String chatId
    ) {
        var service = planService.findServiceById(serviceId);

        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        if (!serviceKey.equals(service.getServiceKey())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        service.setAuthorizationData(AuthorizationData.builder()
                .discordId(discordId)
                .categoryId(categoryId)
                .authorizationChatId(chatId)
                .build());
        planService.savePlanService(service);

        return ResponseEntity.ok(paymentService.generateAuthorizationUrl(service));
    }

    /**
     * Após a autorização do usuário pelo link do Mercado Pago,
     * o mesmo será redirecionado para este endpoint para a ativação
     * e obtenção do Access Token do Mercado Pago.
     * <p>
     * Após obter o Access Token, o salvamos no objeto AuthorizationData
     * do ServiceModel e guardamos no banco de dados.
     * <p>
     * Após tudo isso, avisamos na queue ("authorizer.discord") que o
     * serviço foi autorizado com êxito, para que o discord possa avisar
     * ao usuário.
     *
     * @param code  código gerado pelo Mercado Pago necessário para obtenção
     *              do Access Token para formação de pagamentos posteriores
     * @param state ID do ServiceModel
     * @return
     */
    @GetMapping("/validate")
    public ResponseEntity<Void> authorizationService(
            @RequestParam("code") String code,
            @RequestParam("state") String state
    ) {
        var service = planService.findServiceById(state);

        if (service == null) {
            return ResponseEntity.notFound().build();
        }
        service.getAuthorizationData().setAccessToken(paymentService.generateAccessToken(code));
        planService.savePlanService(service);
        paymentAuthorizeProducer.publishPaymentAuthorizeResponse(service);

        return ResponseEntity.ok().build();
    }
}