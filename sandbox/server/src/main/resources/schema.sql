CREATE TABLE IF NOT EXISTS cars (
  id int auto_increment PRIMARY KEY,
  name VARCHAR(50),
  price INT8,
  "year" DATE
);

CREATE TABLE IF NOT EXISTS persons (
  id int auto_increment PRIMARY KEY,
  first_name VARCHAR(50),
  last_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS actors (
  id int auto_increment PRIMARY KEY,
  first_name VARCHAR(50),
  last_name VARCHAR(50),
  country VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS roles (
  id int auto_increment PRIMARY KEY,
  name VARCHAR(50),
  actor_id int REFERENCES actors (id) on delete CASCADE
);

CREATE TABLE IF NOT EXISTS "user" (
  id int auto_increment PRIMARY KEY,
  "username" VARCHAR(50)
);

INSERT INTO cars (name, price, "year") SELECT 'Audi', 60000, '2007-01-01' WHERE not exists (SELECT name FROM cars WHERE name = 'Audi');
INSERT INTO cars (name, price, "year") SELECT 'BMW', 64000, '2008-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'BMW');
INSERT INTO cars (name, price, "year") SELECT 'Mercedes Benz', 53000, '2013-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Mercedes Benz');
INSERT INTO cars (name, price, "year") SELECT 'Toyota', 94000, '1981-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Toyota');
INSERT INTO cars (name, price, "year") SELECT 'Honda', 161000, '2014-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Honda');
INSERT INTO cars (name, price, "year") SELECT 'Mazda', 40000, '1994-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Mazda');
INSERT INTO cars (name, price, "year") SELECT 'Nissan', 52000, '2012-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Nissan');
INSERT INTO cars (name, price, "year") SELECT 'Renault', 60000, '2013-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Renault');
INSERT INTO cars (name, price, "year") SELECT 'Peugeot', 65000, '2014-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Peugeot');
INSERT INTO cars (name, price, "year") SELECT 'Citroen', 67000, '2012-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Citroen');
INSERT INTO cars (name, price, "year") SELECT 'Ford', 77000, '2014-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Ford');
INSERT INTO cars (name, price, "year") SELECT 'Chevrolet', 32000, '2015-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Chevrolet');
INSERT INTO cars (name, price, "year") SELECT 'Opel', 21000, '2010-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Opel');

INSERT INTO persons (first_name, last_name) VALUES ('Anna', 'Smith');
INSERT INTO persons (first_name, last_name) VALUES ('Mike', 'Rodgers');
INSERT INTO persons (first_name, last_name) VALUES ('Stan', 'Marks');
INSERT INTO persons (first_name, last_name) VALUES ('Rob', 'George');
INSERT INTO persons (first_name, last_name) VALUES ('Ben', 'Smith');


INSERT INTO "user" ("username") VALUES ( 'Anna');