package com.apollodiscord.sdk.entities.services;

import com.apollodiscord.sdk.entities.user.ApolloUser;

import java.util.Calendar;
import java.util.List;

public interface ServiceClient {

    String getServiceId();

    ApolloUser getOwner();

    List<Product> getProducts();

    List<Coupon> getCoupons();

    String getPixKey();

    String getDiscordId();

    String getAccessToken();

    boolean isSuspended();

    Calendar getCreateAt();

    Calendar getExpirateAt();
}