DELETE
FROM product;

INSERT INTO product (id, name, price, quantity, create_time, update_time, deleted)
VALUES (1,'苹果',1000,15,'2019-11-08 15:13:49','2019-11-08 15:13:49',0),
        (2,'西瓜',3000,2,'2019-11-08 15:14:15','2019-11-08 15:14:15',0),
        (3,'被删除的东西',5000,2,'2019-11-08 15:14:34','2019-11-08 15:14:34',1);
