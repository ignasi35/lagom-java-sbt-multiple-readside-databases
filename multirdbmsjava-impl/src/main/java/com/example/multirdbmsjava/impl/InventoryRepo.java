package com.example.multirdbmsjava.impl;

import play.db.Database;
import play.db.NamedDatabase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
CREATE TABLE `inventory` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`sku_id` int(11) DEFAULT NULL,
`count` int(11) DEFAULT NULL,
`name` text,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `inventory` (`id`, `sku_id`, `count`, `name`)
VALUES
	(1, 1, 5, 'tables');
-- inserts a row to state there are 5 tables in the inventory

 */
@Singleton
public class InventoryRepo {

    private Database inventoryDb;

    @Inject
    public InventoryRepo(@NamedDatabase("inventory") Database inventoryDb) {
        this.inventoryDb = inventoryDb;
    }

    public int count() {

        // Don't do this! Use Supply CompletableFuture#supplyAsync !!
        return inventoryDb.withConnection((conn) -> {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT sum(count) from inventory;");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        });
    }

}
