package com.microservice.discord.services.discord.listeners;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BaseListener<T> {

    private String id;

    public BaseListener() {}

    public abstract void execute(T t);
}