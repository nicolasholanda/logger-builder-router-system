CREATE TABLE log_destination (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    type    VARCHAR(50)  NOT NULL,
    config  TEXT,
    async   BOOLEAN      NOT NULL DEFAULT FALSE,
    enabled BOOLEAN      NOT NULL DEFAULT TRUE
);
