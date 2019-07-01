CREATE TABLE IF NOT EXISTS car (
  id int auto_increment PRIMARY KEY,
  name CHAR(50),
  price INT8,
  year DATE
);

INSERT INTO car VALUES (null, 'Audi', 60000, '2007-01-01');
INSERT INTO car VALUES (null, 'BMW', 64000, '2008-01-01');
INSERT INTO car VALUES (null, 'Mercedes Benz', 53000, '2013-01-01');
INSERT INTO car VALUES (null, 'Toyota', 94000, '1981-01-01');
INSERT INTO car VALUES (null, 'Honda', 161000, '2014-01-01');
INSERT INTO car VALUES (null, 'Mazda', 40000, '1994-01-01');
INSERT INTO car VALUES (null, 'Nissan', 52000, '2012-01-01');
INSERT INTO car VALUES (null, 'Renault', 60000, '2013-01-01');
INSERT INTO car VALUES (null, 'Peugeot', 65000, '2014-01-01');
INSERT INTO car VALUES (null, 'Citroen', 67000, '2012-01-01');
INSERT INTO car VALUES (null, 'Ford', 77000, '2014-01-01');
INSERT INTO car VALUES (null, 'Chevrolet', 32000, '2015-01-01');
INSERT INTO car VALUES (null, 'Opel', 21000, '2010-01-01');
