CREATE TABLE t_person
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  password VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX un01_person ON t_person (username);
CREATE TABLE t_bank
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(255)
);
CREATE TABLE t_bankaccount
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  number VARCHAR(255) NOT NULL,
  administrator_id INT(11) NOT NULL,
  bank_id INT(11),
  owner_id INT(11),
  CONSTRAINT fk01_bankaccount FOREIGN KEY (administrator_id) REFERENCES t_person (id),
  CONSTRAINT fk02_bankaccount FOREIGN KEY (bank_id) REFERENCES t_bank (id),
  CONSTRAINT fk03_bankaccount FOREIGN KEY (owner_id) REFERENCES t_person (id)
);
CREATE INDEX I01_bankaccount ON t_bankaccount (bank_id);
CREATE INDEX I02_bankaccount ON t_bankaccount (administrator_id);
CREATE INDEX I03_bankaccount ON t_bankaccount (owner_id);
CREATE UNIQUE INDEX un_bankaccount ON t_bankaccount (number, administrator_id);
CREATE TABLE t_car
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  brand VARCHAR(255),
  fuelType INT(11),
  model VARCHAR(255),
  purchaseDate DATE,
  purchasePrice DOUBLE,
  owner_id INT(11),
  CONSTRAINT fk01_car FOREIGN KEY (owner_id) REFERENCES t_person (id)
);
CREATE INDEX I01_car ON t_car (owner_id);
CREATE TABLE t_category
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(255)
);
CREATE TABLE t_financial_instrument
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  quote VARCHAR(255)
);
CREATE UNIQUE INDEX un01_financial_instrument ON t_financial_instrument (quote);
CREATE TABLE t_fund_purchase
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATE NOT NULL,
  numberOfShares INT(11) NOT NULL,
  quote VARCHAR(255),
  sharePrice DOUBLE NOT NULL,
  transactionCost DOUBLE,
  person_id INT(11),
  CONSTRAINT fk01_fund_purchase FOREIGN KEY (person_id) REFERENCES t_person (id)
);
CREATE INDEX I01_fund_purchase ON t_fund_purchase (person_id);
CREATE TABLE t_historic_price
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATE,
  price DOUBLE,
  financial_instrument_id INT(11) NOT NULL,
  CONSTRAINT fk01_historic_price FOREIGN KEY (financial_instrument_id) REFERENCES t_financial_instrument (id)
);
CREATE UNIQUE INDEX un_historic_price_01 ON t_historic_price (financial_instrument_id, date);
CREATE TABLE t_refueling
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATE,
  kilometres DOUBLE,
  liters DOUBLE,
  price DOUBLE,
  pricePerLiter DOUBLE,
  car_id INT(11),
  fuelTankFull BIT(1) NOT NULL ,
  CONSTRAINT fk01_refueling FOREIGN KEY (car_id) REFERENCES t_car (id)
);
CREATE INDEX I01_refueling ON t_refueling (car_id);
CREATE TABLE t_statement
(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  amount DECIMAL(19,2),
  date DATE,
  destinationAccount_id INT(11),
  originatingAccount_id INT(11),
  CONSTRAINT fk01_statement FOREIGN KEY (destinationAccount_id) REFERENCES t_bankaccount (id),
  CONSTRAINT fk02_statement FOREIGN KEY (originatingAccount_id) REFERENCES t_bankaccount (id)
);
CREATE INDEX I01_statement ON t_statement (destinationAccount_id);
CREATE INDEX I02_statement ON t_statement (originatingAccount_id);