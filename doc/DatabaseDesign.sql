-- Database
CREATE DATABASE rate;

USE rate;
-- Main Table
CREATE TABLE usd_cny(
id int NOT NULL,
date DATE,
rate float,
PRIMARY KEY(id)
);

-- Test Table
CREATE TABLE test(
id int NOT NULL,
PRIMARY KEY(id)
);