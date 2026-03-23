CREATE TABLE log_route (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE log_route_level (
    route_id BIGINT      NOT NULL REFERENCES log_route(id) ON DELETE CASCADE,
    level    VARCHAR(50) NOT NULL,
    PRIMARY KEY (route_id, level)
);

CREATE TABLE log_route_destination (
    route_id       BIGINT NOT NULL REFERENCES log_route(id) ON DELETE CASCADE,
    destination_id BIGINT NOT NULL REFERENCES log_destination(id) ON DELETE CASCADE,
    PRIMARY KEY (route_id, destination_id)
);
