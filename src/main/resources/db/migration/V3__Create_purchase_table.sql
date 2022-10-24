-- We could as well store purchases in one table (eg. 'item') together with services.
-- That table would have to has an additional discriminator column eg. 'type' = SERVICE or PURCHASE.
-- That solution would possibly have a better performance, but it assume that services and purchases will always have same properties.
-- This change can be easily implemented in any moment, as it doesn't affect domain or anything except JDBC adapter of Appointments.
CREATE TABLE purchase
(
    id             UUID PRIMARY KEY,
    appointment_id UUID         NOT NULL,
    name           VARCHAR(100) NOT NULL,
    price          NUMERIC      NOT NULL,
    loyalty_points INTEGER      NOT NULL,

    CONSTRAINT fk_appointment FOREIGN KEY (appointment_id) REFERENCES appointment (id) ON DELETE CASCADE
)