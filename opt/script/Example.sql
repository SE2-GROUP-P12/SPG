INSERT INTO `spg`.`user`
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

INSERT INTO `spg`.`user`
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

INSERT INTO `spg`.`user`
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

INSERT INTO `spg`.`wallet`
(`user_id`,
`value`)
VALUES
(2,
50.0);

INSERT INTO `spg`.`product`
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

INSERT INTO `spg`.`product`
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

INSERT INTO `spg`.`product`
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

INSERT INTO `spg`.`basket`
(`user_id`,
`product_id`,
`quantity`)
VALUES
(1,
1,
10);








