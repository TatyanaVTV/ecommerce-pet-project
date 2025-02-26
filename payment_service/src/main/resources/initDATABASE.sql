CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    sum NUMERIC(15, 2) NOT NULL,
    status INT NOT NULL
);
