CREATE TABLE unverified_users (
    email VARCHAR(255) UNIQUE NOT NULL,
    verification_token VARCHAR(255) UNIQUE NOT NULL,
    registration_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
