package com.apollo.microservice.service.services;

import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.models.CouponModel;
import com.apollo.microservice.service.models.PaymentModel;
import com.apollo.microservice.service.models.ProductModel;
import com.apollo.microservice.service.models.ServiceModel;
import com.apollo.microservice.service.repositories.CouponRepository;
import com.apollo.microservice.service.repositories.PaymentRepository;
import com.apollo.microservice.service.repositories.ProductRepository;
import com.apollo.microservice.service.repositories.ServiceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PlanService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CouponRepository couponRepository;

    public Page<ServiceModel> getPageableServices(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    public Page<PaymentModel> getPageablePayments(Pageable pageable) {
        return paymentRepository.findAllNonExpired(pageable);
    }

    public Page<PaymentModel> getPageablePaymentsByServiceId(Pageable pageable, String serviceId) {
        return paymentRepository.findAllByServiceId(pageable, serviceId);
    }

    public CouponModel createNewCoupon(CouponDTO couponDTO, ServiceModel serviceModel) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, couponDTO.getExpirateDays());
        var coupon = CouponModel.builder()
                .name(couponDTO.getName())
                .usage(0)
                .discount(couponDTO.getDiscount())
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .service(serviceModel)
                .maxUsage(couponDTO.getMaxUsage() != null ? couponDTO.getMaxUsage() : -1)
                .isExpired(false)
                .build();

        couponRepository.saveAndFlush(coupon);
        return coupon;
    }

    public CouponModel findByCouponId(long id) {
        return couponRepository.findById(id).orElse(null);
    }

    public ProductModel createNewProduct(ProductDTO productDTO, ServiceModel serviceModel) {
        var product = ProductModel.builder()
                .name(productDTO.getName().toUpperCase())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .service(serviceModel)
                .createAt(Calendar.getInstance())
                .build();

        productRepository.saveAndFlush(product);
        return product;
    }

    public ProductModel findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public ServiceModel createNewService(String owner) {
        var service = ServiceModel.builder()
                .owner(owner)
                .serviceKey(RandomStringUtils.randomAlphanumeric(8))
                .createAt(Calendar.getInstance())
                .build();
        serviceRepository.saveAndFlush(service);
        return service;
    }

    public ServiceModel findByDiscordId(String discordId) {
        return serviceRepository.findByDiscordId(discordId).orElse(null);
    }

    public ServiceModel findServiceById(String id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public ServiceModel findByOwner(String owner) {
        return serviceRepository.findByOwner(owner).orElse(null);
    }

    public void deleteProductById(long id) {
        productRepository.deleteById(id);
    }

    public void deleteCouponById(long id) {
        couponRepository.deleteById(id);
    }

    public void savePlanService(ServiceModel serviceModel) {
        serviceRepository.saveAndFlush(serviceModel);
    }
}
