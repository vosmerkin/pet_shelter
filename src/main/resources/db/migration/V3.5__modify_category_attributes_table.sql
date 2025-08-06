ALTER TABLE category_attributes ADD COLUMN id BIGINT;

-- Step 2: Assign unique sequential IDs using ROW_NUMBER() via UPDATE FROM
WITH row_numbers AS (
    SELECT
        category_id,
        attribute_id,
        ROW_NUMBER() OVER () AS rn
    FROM category_attributes
)
UPDATE category_attributes
SET id = row_numbers.rn
FROM row_numbers
WHERE category_attributes.category_id = row_numbers.category_id
  AND category_attributes.attribute_id = row_numbers.attribute_id;

-- Step 3: Create a sequence for future auto-increment
CREATE SEQUENCE IF NOT EXISTS category_attributes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Step 4: Set the sequence to current max id
SELECT SETVAL('category_attributes_id_seq', COALESCE(MAX(id), 1)) FROM category_attributes;

-- Step 5: Set id to use the sequence and be NOT NULL
ALTER TABLE category_attributes
ALTER COLUMN id SET DEFAULT nextval('category_attributes_id_seq'),
ALTER COLUMN id SET NOT NULL;

-- Step 6: Drop the old primary key (usually named after the constraint)
-- You can find the name via: \d category_attributes in psql
-- Common default name: "category_attributes_pkey"
ALTER TABLE category_attributes DROP CONSTRAINT IF EXISTS category_classification_attributes_pkey;

-- Step 7: Add new primary key on id
ALTER TABLE category_attributes ADD CONSTRAINT category_attributes_pkey PRIMARY KEY (id);

-- Step 8: Ensure (category_id, attribute_id) is unique
ALTER TABLE category_attributes
ADD CONSTRAINT uk_category_attribute UNIQUE (category_id, attribute_id);