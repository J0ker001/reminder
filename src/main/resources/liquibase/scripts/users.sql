-- liquibase formatted sql

-- changeset lanton:createTableUsers

CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       varchar(2000),
    name        varchar(255),
    provider_ID  varchar,
    telegram_ID varchar(2000)
);