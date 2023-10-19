package com.apollo.microservice.service;

import com.apollo.microservice.service.enums.PaymentIntent;
import com.apollo.microservice.service.enums.PaymentStatus;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.CouponRepository;
import com.apollo.microservice.service.repositories.PaymentRepository;
import com.apollo.microservice.service.repositories.ProductRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;

@SpringBootTest
class ServiceCRUDTests {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ServiceRepository serviceRepository;

	@Test
	void contextLoads() {
		var service = ServiceModel.builder()
				.isSuspended(false)
				.owner("viniciusalb10@gmail.com")
				.discordId("24819248103912830913")
				.pixKey("2091849128391381293")
				.accessToken("201489248129318390183120938")
				.createAt(Calendar.getInstance())
				.expirateAt(Calendar.getInstance())
				.build();

		serviceRepository.saveAndFlush(service);

		var coupon = CouponModel.builder()
				.name("NATAL20")
				.serviceId(service.getId())
				.isEnabled(true)
				.discount(20)
				.createAt(Calendar.getInstance())
				.expirateAt(Calendar.getInstance())
				.build();

		var product = ProductModel.builder()
				.name("VIP")
				.serviceId(service.getId())
				.price(1.0)
				.description("Obtenha acesso a vantagem")
				.createAt(Calendar.getInstance())
				.build();

		couponRepository.saveAndFlush(coupon);
		productRepository.saveAndFlush(product);

		var payment = PaymentModel.builder()
				.payer("viniciusalb10@gmail.com")
				.coupon(null)
				.productId(product.getId())
				.paymentIntent(PaymentIntent.SELL_PRODUCT)
				.paymentStatus(PaymentStatus.PAYED)
				.createAt(Calendar.getInstance())
				.expirateAt(Calendar.getInstance())
				.build();

		paymentRepository.saveAndFlush(payment);

		System.out.println(coupon);
		System.out.println(product);
		System.out.println(service);
		System.out.println(payment);

		couponRepository.delete(coupon);
		productRepository.delete(product);
		serviceRepository.delete(service);
		paymentRepository.delete(payment);
	}
}
