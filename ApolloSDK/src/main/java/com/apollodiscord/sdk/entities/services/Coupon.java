package com.apollodiscord.sdk.entities.services;

import java.util.Calendar;

public interface Coupon {

    String getName();

    Integer getPercentage();

    boolean isSuspended();

    Calendar getCreateAt();

    Calendar getExpirateAt();
}