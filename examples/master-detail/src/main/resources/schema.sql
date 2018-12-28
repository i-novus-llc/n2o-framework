CREATE TABLE IF NOT EXISTS CATEGORY (
  id int auto_increment PRIMARY KEY,
  name CHAR(50)
);

CREATE TABLE IF NOT EXISTS SHOP (
  id int auto_increment PRIMARY KEY,
  name CHAR(50),
  address CHAR(100)
);

CREATE TABLE IF NOT EXISTS PRODUCT (
  id int auto_increment PRIMARY KEY,
  name CHAR(50),
  price INT8,
  description CHAR(150),
  discount CHAR(30),
  category_id INT4 REFERENCES CATEGORY(id)
);

CREATE TABLE IF NOT EXISTS PRODUCT_SHOP (
  product_id INT4 REFERENCES PRODUCT(id) ON DELETE CASCADE,
  shop_id INT4 REFERENCES SHOP(id) ON DELETE CASCADE,
);


INSERT INTO CATEGORY (id, name) VALUES (1, 'Electronics');
INSERT INTO CATEGORY (id, name) VALUES (2, 'Furniture');


INSERT INTO SHOP (id, name, address) VALUES (1, 'Eldorado', 'Kazan, Volkova 7');
INSERT INTO SHOP (id, name, address) VALUES (2, 'Atlant', 'Kazan, Saharova 27');
INSERT INTO SHOP (id, name, address) VALUES (3, 'DNS', 'Kazan, pr. Pobedy 1');
INSERT INTO SHOP (id, name, address) VALUES (4, 'Mebelgrad', 'Kazan, Sovetskay 5');
INSERT INTO SHOP (id, name, address) VALUES (5, 'Mega-Mebel', 'Kazan, Sechenova 4');


INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (1, 'Phone', 35, 'Motorola Z310', '5%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (2, 'Laptop', 71, 'Lenovo S-2500', '3%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (3, 'TV', 200, 'LG, diagonal: 25', '35%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (4, 'Sega', 20, 'Special offer: additional 5 cartridges!', '0%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (5, 'Smart home', 500, 'Best system to manage your house', '7%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (6, 'Computer', 150, 'Apple iMac 21.5', '5%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (7, 'Smart vacuum cleaner', 100, 'LG VR6270LVM', '5%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (8, 'Microwave oven', 175, 'The best oven!', '5%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (9, 'Climate control', 370, 'Fill you in a tropic', '0%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (10, 'Dishwasher', 210, 'Save your time', '70%', 1);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (11, 'Home helper robot', 1350, 'Now you can talk not only with cats or mirror', '5%', 1);

INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (12, 'Sofa', 155, 'Very comfortable', '10%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (13, 'Chair', 50, 'Best place to have a rest', '15%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (14, 'Table', 170, 'Big table for big family', '5%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (15, 'Cupboard', 500, 'Easy fit all your clothe', '5%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (16, 'Mirror', 35, 'Anti-glare', '8%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (17, 'Computer table', 100, 'Standard size', '5%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (18, 'Shelf', 10, 'Big choice', '5%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (19, 'Armchair', 80, 'Soft and style', '5%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (20, 'Bed', 320, 'For two people', '18%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (21, 'Nightstand', 30, 'Small size', '5%', 2);
INSERT INTO PRODUCT (id, name, price, description, discount, category_id) VALUES (22, 'Wardrobe', 110, 'For small room', '20%', 2);


INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('1', '1');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('1', '2');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('1', '3');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('2', '1');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('2', '2');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('3', '3');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('3', '2');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('4', '1');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('5', '3');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('6', '1');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('6', '2');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('6', '3');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('7', '2');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('11', '3');

INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('11', '5');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('12', '4');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('14', '5');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('15', '4');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('15', '5');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('16', '4');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('17', '3');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('18', '4');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('19', '5');
INSERT INTO PRODUCT_SHOP (PRODUCT_ID, SHOP_ID) VALUES ('20', '5');