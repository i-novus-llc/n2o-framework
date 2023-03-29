CREATE TABLE IF NOT EXISTS cars (
  id int auto_increment PRIMARY KEY,
  name CHAR(50),
  price INT8,
  year DATE
);

CREATE TABLE IF NOT EXISTS persons (
  id int auto_increment PRIMARY KEY,
  first_name CHAR(50),
  last_name CHAR(50)
);

CREATE TABLE IF NOT EXISTS actors (
  id int auto_increment PRIMARY KEY,
  first_name CHAR(50),
  last_name CHAR(50),
  country CHAR(50)
);

CREATE TABLE IF NOT EXISTS roles (
  id int auto_increment PRIMARY KEY,
  name CHAR(50),
  actor_id int REFERENCES actors (id) on delete CASCADE
);

INSERT INTO cars (name, price, year) SELECT 'Audi', 60000, '2007-01-01' WHERE not exists (SELECT name FROM cars WHERE name = 'Audi');
INSERT INTO cars (name, price, year) SELECT 'BMW', 64000, '2008-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'BMW');
INSERT INTO cars (name, price, year) SELECT 'Mercedes Benz', 53000, '2013-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Mercedes Benz');
INSERT INTO cars (name, price, year) SELECT 'Toyota', 94000, '1981-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Toyota');
INSERT INTO cars (name, price, year) SELECT 'Honda', 161000, '2014-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Honda');
INSERT INTO cars (name, price, year) SELECT 'Mazda', 40000, '1994-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Mazda');
INSERT INTO cars (name, price, year) SELECT 'Nissan', 52000, '2012-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Nissan');
INSERT INTO cars (name, price, year) SELECT 'Renault', 60000, '2013-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Renault');
INSERT INTO cars (name, price, year) SELECT 'Peugeot', 65000, '2014-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Peugeot');
INSERT INTO cars (name, price, year) SELECT 'Citroen', 67000, '2012-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Citroen');
INSERT INTO cars (name, price, year) SELECT 'Ford', 77000, '2014-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Ford');
INSERT INTO cars (name, price, year) SELECT 'Chevrolet', 32000, '2015-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Chevrolet');
INSERT INTO cars (name, price, year) SELECT 'Opel', 21000, '2010-01-01'  WHERE not exists (SELECT name FROM cars WHERE name = 'Opel');

INSERT INTO persons VALUES (null, 'Anna', 'Smith');
INSERT INTO persons VALUES (null, 'Mike', 'Rodgers');
INSERT INTO persons VALUES (null, 'Stan', 'Marks');
INSERT INTO persons VALUES (null, 'Rob', 'George');
INSERT INTO persons VALUES (null, 'Ben', 'Smith');