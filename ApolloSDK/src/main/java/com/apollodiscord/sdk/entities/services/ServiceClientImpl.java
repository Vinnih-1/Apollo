package com.apollodiscord.sdk.entities.services;

import com.apollodiscord.sdk.entities.user.ApolloUserImpl;
import lombok.Builder;
import lombok.Getter;

import java.util.Calendar;
import java.util.List;

@Getter
@Builder
public class ServiceClientImpl implements ServiceClient {

    private String serviceId;

    private ApolloUserImpl owner;

    private List<Product> products;

    private List<Coupon> coupons;

    private String pixKey;

    private String discordId;

    private String accessToken;

    private boolean isSuspended;

    private Calendar createAt;

    private Calendar expirateAt;
}