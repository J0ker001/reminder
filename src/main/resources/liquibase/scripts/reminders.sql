-- liquibase formatted sql

-- changeset lanton:createTableReminders

CREATE TABLE IF NOT EXISTS reminders
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       varchar(255) NOT NULL,
    description varchar(4096),
    remind      timestamp,
    user_id     bigint REFERENCES users (id) on update cascade on delete cascade,
    is_sent     boolean
)