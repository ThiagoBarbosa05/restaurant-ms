ALTER TABLE ingredients
ADD COLUMN dish_id UUID;

ALTER TABLE ingredients
ADD CONSTRAINT fk_dishes
FOREIGN KEY (dish_id)
REFERENCES dishes (id) ON DELETE CASCADE;