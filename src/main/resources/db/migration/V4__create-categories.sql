CREATE TABLE categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

ALTER TABLE dishes
ADD COLUMN category_id UUID;

ALTER TABLE dishes
ADD CONSTRAINT fk_categories
FOREIGN KEY (category_id)
REFERENCES categories (id);