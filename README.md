# MultiRdbmsJava

This project demonstrates the use of many databases in a single Lagom service using [Play's `javaJdbc`](https://www.playframework.com/documentation/2.6.x/JavaDatabase).

There are some limitations though:

 - only the `default` database can be used in Lagom-managed read side processors. All other databases may be used in read-side processors where user code handles the offset tracking.
 - all databases use the same implementation (RDBMS vs Cassandra). More advanced combinations may be possible but not demoed here.
 
 
## Setup

To make things simple, it is recommended to start a MySQL server locally using docker:

`docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=rootpwd -p3306:3306 -d mysql:5`

Once the MySQL server is running start the application using: 

`sbt runAll`

The JDBC-URLs are prepared to created the schemas:

```
db.default {
  url = "jdbc:mysql://localhost/hello?createDatabaseIfNotExist=true&serverTimezone=UTC"
}
db.orders {
  url = "jdbc:mysql://localhost/orders?createDatabaseIfNotExist=true&serverTimezone=UTC"
}

db.inventory {
  url = "jdbc:mysql://localhost/inventory?createDatabaseIfNotExist=true&serverTimezone=UTC"
}
```

so if you now inspect the database you should see all three databases.

Then, create a table and insert some data on the `inventory` schema:

```sql
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

```
 
Finally, send a request to see how many items are there in the inventory:

```bash
$ curl http://localhost:9000/api/hello/alice
Hello, alice!(5 items in repo)
```

## Inspecting the code:

 * See `InventoryRepo` and `OrderRepo`
 * See how `MultirdbmsjavaModule` binds the two repos as singletons
 * See how `build.sbt` includes a dependency to the MySQL driver and Play's `javaJdbc`