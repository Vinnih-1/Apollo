![apollo-isologo](https://github.com/Vinnih-1/Apollo/assets/59892753/dc6dad52-3a8f-43b0-90f8-26296012e6e2)

O projeto tem a intenção de automatizar o sistema de pagamentos pelo Discord, utilizando da API do Mercado Pago. O projeto foi criado para ser inicialmente pago, porém com o decorrer do tempo, decidi deixá-lo gratuito e open-source.

Projeto back-end separado em 5 microsserviços, contendo eles:

- [Eureka](https://github.com/Vinnih-1/Apollo/tree/develop/ApolloEureka)
- [Gateway](https://github.com/Vinnih-1/Apollo/tree/develop/Gateway)
- [Authentication](https://github.com/Vinnih-1/Apollo/tree/develop/Authentication)
- [Discord](https://github.com/Vinnih-1/Apollo/tree/develop/Discord)
- [Service](https://github.com/Vinnih-1/Apollo/tree/develop/Service)

Contendo também o projeto front-end, para acessar, basta [clicar aqui](https://github.com/Vinnih-1/Apollo-Website)

Rotas do Authentication:

| Endpoint | Método | Autenticação Requerida |
|---------------|--------------|---------------|
| /auth/login   | POST         | NÃO           |
| /auth/register| POST         | NÃO           |
| /auth/validate| GET          | NÃO           |
| /auth/user    | POST         | SIM           |
| /auth/users   | POST         | SIM           |

Rotas do Service:

| Endpoint | Método | Autenticação Requerida |
|-------------------------|--------------|---------------|
| /service/               | GET          | SIM           |
| /service/email          | GET          | SIM           |
| /service/payments       | GET          | SIM           |
| /service/plans          | GET          | SIM           |
| /service/discord        | GET          | NÃO           |
| /service/product/       | GET          | SIM           |
| /service/product/create | POST         | SIM           |
| /service/product/delete | DELETE       | SIM           |
| /service/coupon/        | GET          | SIM           |
| /service/coupon/create  | POST         | SIM           |
| /service/coupon/delete  | DELETE       | SIM           |
| /service/authorize      | GET          | NÃO           |
| /service/validate       | GET          | NÃO           |

Cada usuário poderá ter um plano (também chamado de Serviço), que é criado ao usuário registrar sua conta no [site](https://github.com/Vinnih-1/Apollo-Website). Lá ele terá acesso ao dashboard onde poderá cria criar ou excluir produtos ou cupons de desconto. Tabmém poderá observar pagamentos criados e finalizados em seu plano.

Após o usuário se cadastrar e entrar na dashboard, ele deverá entrar na seção de Segurança para poder convidar o bot do [Discord](https://github.com/Vinnih-1/Apollo/tree/develop/Discord) para o discord onde irá realizar as vendas de seus produtos. Logo em seguida, precisará autorizar a aplicação do projeto para poder criar e observar pagamentos na API do Mercado Pago.

Comandos do Discord:

| Comando      | Permissão    |
|--------------|--------------|
| /autorizar   | ADMIN        |
| /menu        | ADMIN        |

Depois da autorização, a categoria de vendas é criada e o usuário está apto para usar o comando /menu e vender seus produtos.

O projeto tem o domínio registrado apollodiscord.com, porém não há recursos para mantê-lo de pé. Então decidi continuar o desenvolvimento apenas para aprendizado.
Ainda em fase de desenvolvimento, qualquer pull request será bem-vindo.

PODE HAVER ATUALIZAÇÕES DE QUEBRA.
