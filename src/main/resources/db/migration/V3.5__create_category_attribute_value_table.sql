CREATE TABLE category_attribute_value (
    category_id                 BIGINT NOT NULL,
    attribute_id                BIGINT NOT NULL,
    value                       VARCHAR(255) NOT NULL,
    PRIMARY KEY (category_id, attribute_id),
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES category (id),
    CONSTRAINT fk_attribute
        FOREIGN KEY (attribute_id)
            REFERENCES attribute (id)
);