CREATE TABLE category_attribute_option (
    id BIGSERIAL PRIMARY KEY,
    category_attribute_id BIGINT NOT NULL,
    value VARCHAR(255) NOT NULL,
    CONSTRAINT uk_category_attr_option UNIQUE (category_attribute_id, value),
    CONSTRAINT fk_category_attr_option_attr
        FOREIGN KEY (category_attribute_id) REFERENCES category_attributes (id)
        ON DELETE CASCADE
);