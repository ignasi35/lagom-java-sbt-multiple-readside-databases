package com.example.multirdbmsjava.impl;

import play.db.NamedDatabase;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OrdersRepo {
    private Database ordersDb;

    @Inject
    public OrdersRepo(@NamedDatabase("orders") Database ordersDb) {
        this.ordersDb = ordersDb;
    }
}
