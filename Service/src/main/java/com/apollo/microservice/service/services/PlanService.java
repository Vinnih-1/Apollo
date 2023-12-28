package com.apollo.microservice.service.services;

import com.apollo.microservice.service.dtos.CouponDTO;
import com.apollo.microservice.service.dtos.ProductDTO;
import com.apollo.microservice.service.enums.PaymentStatus;
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
import java.util.List;

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

    public List<PaymentModel> getPaymentsFromService(String serviceId, PaymentStatus paymentStatus) {
        return paymentRepository.findAllFilteredByServiceId(serviceId, paymentStatus).orElse(null);
    }

    public List<PaymentModel> getPaymentsFromService(String serviceId) {
        return paymentRepository.findAllByServiceId(serviceId).orElse(null);
    }

    public Page<ServiceModel> getPageableServices(Pageable pageable) {
        return serviceRepository.findAll(pageable);
    }

    public List<CouponModel> getCouponsFromService(String serviceId) {
        return couponRepository.findCouponsByServiceId(serviceId).orElse(null);
    }

    public CouponModel createNewCoupon(CouponDTO couponDTO) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, couponDTO.getExpirateDays());
        var coupon = CouponModel.builder()
                .name(couponDTO.getName())
                .discount(couponDTO.getDiscount())
                .serviceId(couponDTO.getServiceId())
                .isEnabled(true)
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .build();
        couponRepository.saveAndFlush(coupon);
        return coupon;
    }

    public CouponModel findByCouponId(long id) {
        return couponRepository.findById(id).orElse(null);
    }

    public ProductModel createNewProduct(ProductDTO productDTO) {
        var product = ProductModel.builder()
                .name(productDTO.getName().toUpperCase())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .service(findServiceById(productDTO.getServiceId()))
                .createAt(Calendar.getInstance())
                .build();
        productRepository.saveAndFlush(product);
        return product;
    }

    public ProductModel findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public ServiceModel createNewService(String owner) {
        var calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        var service = ServiceModel.builder()
                .owner(owner)
                .serviceKey(RandomStringUtils.randomAlphanumeric(8))
                .createAt(Calendar.getInstance())
                .expirateAt(calendar)
                .isSuspended(false)
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
}
