CREATE TABLE log_entry (
    id             BIGSERIAL    PRIMARY KEY,
    level          VARCHAR(50)  NOT NULL,
    message        TEXT         NOT NULL,
    source         VARCHAR(255),
    destination_id BIGINT       REFERENCES log_destination(id) ON DELETE SET NULL,
    timestamp      TIMESTAMPTZ  NOT NULL,
    status         VARCHAR(50)  NOT NULL
);
