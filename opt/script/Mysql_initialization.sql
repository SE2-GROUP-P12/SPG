
CREATE TABLE `basket` (
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` float DEFAULT NULL,
  PRIMARY KEY (`user_id`,`product_id`)
) CHARSET=utf8;

CREATE TABLE `product` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` float DEFAULT NULL,
  `unit_of_measurement` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) CHARSET=utf8;

CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `ssn` varchar(45) DEFAULT NULL,
  `phone_number` int DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) CHARSET=utf8;

CREATE TABLE `wallet` (
  `user_id` int NOT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) CHARSET=utf8;
