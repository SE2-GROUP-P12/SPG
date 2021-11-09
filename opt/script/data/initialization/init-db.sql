
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

INSERT INTO `user`
(`user_id`,
`name`,
`surname`,
`ssn`,
`phone_number`,
`email`,
`password`,
`role`)
VALUES
(1,
"Mario",
"Rossi",
"RSSMRA00D12N376V",
01234567892,
"mario.rossi@gmail.com",
null,
"client");

INSERT INTO `user`
(`user_id`,
`name`,
`surname`,
`ssn`,
`phone_number`,
`email`,
`password`,
`role`)
VALUES
(2,
"Paolo",
"Bianchi",
"BNCPLA00D12N376V",
01234567892,
"paolo.bianchi@gmail.com",
null,
"client");

INSERT INTO `user`
(`user_id`,
`name`,
`surname`,
`ssn`,
`phone_number`,
`email`,
`password`,
`role`)
VALUES
(3,
"Francesco",
"Conte",
"CNTFRN00D12N376V",
01234567892,
"francesco.conte@gmail.com",
null,
"employee");

INSERT INTO `wallet`
(`user_id`,
`value`)
VALUES
(2,
50.0);

INSERT INTO `product`
(`product_id`,
`name`,
`quantity`,
`price`,
`unit_of_measurement`)
VALUES
(1,
"Mele",
50,
2.50,
"Kg");

INSERT INTO `product`
(`product_id`,
`name`,
`quantity`,
`price`,
`unit_of_measurement`)
VALUES
(2,
"Farina",
10,
5.00,
"Kg");

INSERT INTO `product`
(`product_id`,
`name`,
`quantity`,
`price`,
`unit_of_measurement`)
VALUES
(3,
"Uova",
36,
6.25,
"Unità");


INSERT INTO `basket`
(`user_id`,
`product_id`,
`quantity`)
VALUES
(1,
1,
10);
