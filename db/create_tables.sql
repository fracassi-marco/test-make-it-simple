CREATE DATABASE shop;

\c shop

CREATE TABLE products (
     name               varchar(128) PRIMARY KEY,
     description        varchar(512) NOT NULL,
     image              varchar(1024),
     available_quantity  integer NOT NULL,
     booked_quantity     integer NOT NULL
);

CREATE TABLE cart (
     name   varchar(128),
     price  integer
);