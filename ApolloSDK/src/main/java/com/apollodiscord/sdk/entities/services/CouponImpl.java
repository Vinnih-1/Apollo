package com.apollodiscord.sdk.entities.services;

import lombok.Builder;
import lombok.Getter;

import java.util.Calendar;

@Getter
@Builder
public class CouponImpl implements Coupon {

    private String name;

    private Integer percentage;

    private boolean isSuspended;

    private Calendar createAt;

    private Calendar expirateAt;
}