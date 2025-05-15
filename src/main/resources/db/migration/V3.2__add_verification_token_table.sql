CREATE TABLE verification_token (
    id                 BIGSERIAL PRIMARY KEY,
    verification_token VARCHAR(255) UNIQUE NOT NULL,
    user_id            BIGINT         NOT NULL,
    expiry_date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
