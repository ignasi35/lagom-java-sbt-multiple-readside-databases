package com.example.multirdbmsjava.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import com.example.multirdbmsjava.api.MultirdbmsjavaService;

/**
 * The module that binds the MultirdbmsjavaService so that it can be served.
 */
public class MultirdbmsjavaModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(MultirdbmsjavaService.class, MultirdbmsjavaServiceImpl.class);
        bind(OrdersRepo.class).asEagerSingleton();
        bind(InventoryRepo.class).asEagerSingleton();
    }
}
