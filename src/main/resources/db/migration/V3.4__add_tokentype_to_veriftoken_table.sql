ALTER TABLE verification_token
    ADD COLUMN token_type VARCHAR(255) DEFAULT 'REGISTRATION' NOT NULL;
