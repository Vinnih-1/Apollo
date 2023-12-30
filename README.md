Missão atual: Fundir o microsserviço Payments com o Service

Motivação:

	Atualmente o microsserviço Payments não tem uma função tão complexa a ponto de ser necessário
	o separo em outro microsserviço. A sua missão atual é receber os pedidos de geração de pagamento
	requisitado pelo discord e acessar a API do Mercado Pago para obter o QRCode e o QRCodeBase64.
	
	Acredito que o sistema ficará mais simples se reduzir este microsserviço desnecessário e
	introduzí-lo no microsserviço de serviços. Além de reduzir o número de queues necessárias
	para a geração de pagamentos no RabbitMQ, pois, se for introduzido, conterá apenas as queues
	de 'payment.discord' e 'producer.payment'.

Tarefas na cronologia correta:

	- Remodelar os objetos e suas relações no banco de dados
		- ServiceModel
		- ProductModel
		- CouponModel
		- PaymentModel

	- Gerar seus respectivos DTOs sem os dados sensíveis, como por exemplo: AuthorizationData

	- Remodelar os controllers para a geração de pagamentos

	- Remodelar PaymentService e introduzir o conceito de PaymentObserver para observar o status dos pagamentos gerados

	- Atualizar dados do Eureka e Gateway para remoção completa do microsserviço Payments.