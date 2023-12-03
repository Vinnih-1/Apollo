# Apollo
All microservices that compose the backend of Apollo

# Routes

- Authentication:
- - /auth/login [email and password are required fields]
  - /auth/register [email and password are required fields]
  - /auth/validate [this field contains only the token]
 
# After authentication, you need to create a service that you can sell your products.
### You must be authenticated to use all these endpoints

- Services:
- - /service/create [owner and serviceType are required fields]
  - /service/edit [owner and serviceType are required fields]
  - /service/suspend [owner and serviceType are required fields]
  - /service/{id} [the path variable contains only the service id]
  - /service/discord/{id} [the path variable contains only the service id]

   ### After created your service, you need to authorize us to use your account to generate all payments directed to your Mercado Pago account.
 - Services:
 - - /service/authorize [this endpoint contains many request params (serviceId, serviceKey, discordId, categoryId, chatId)]
   ### After do this, you will receive a link to authorize us and will be redirected to `/service/validate` endpoint.

   # Now you can sell your products, but before that you need to create a product, right?

- Services:
- - /service/product/create [product id is a required field]
  - /service/product/delete [product id is a required field]
  - /service/product/{id}/payment [you need to supply a path variable that is the product id and two request params (payer and chatId)]
