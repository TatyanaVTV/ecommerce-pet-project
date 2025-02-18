CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS recommendations (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    recommended_product_id BIGINT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    score DECIMAL(3,2) CHECK (score BETWEEN 0 AND 1),
);

CREATE INDEX idx_comments_user ON comments (user_id);
CREATE INDEX idx_comments_product ON comments (product_id);
