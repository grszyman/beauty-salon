CREATE TABLE appointment
(
    id         UUID PRIMARY KEY,
    client_id  UUID      NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time   TIMESTAMP NOT NULL,
    version    BIGINT    NOT NULL DEFAULT 0,
    -- TODO: store a sum of loyalty points for a performance improvement

    -- It could also be a weak reference. Each option has its cons and pros, but in general, its a decision between
    -- a strong data consistency and a loose relation between aggregate roots.
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE
);

CREATE TABLE service
(
    id             UUID PRIMARY KEY,
    appointment_id UUID         NOT NULL,
    name           VARCHAR(100) NOT NULL,
    price          NUMERIC      NOT NULL,
    loyalty_points INTEGER      NOT NULL,

    CONSTRAINT fk_appointment FOREIGN KEY (appointment_id) REFERENCES appointment (id) ON DELETE CASCADE
)