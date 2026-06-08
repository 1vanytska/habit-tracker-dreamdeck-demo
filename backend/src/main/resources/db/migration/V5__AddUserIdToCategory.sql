ALTER TABLE category
    ADD COLUMN user_id UUID REFERENCES app_user (id);

UPDATE category
SET user_id = (SELECT id FROM app_user ORDER BY registration_date LIMIT 1)
WHERE user_id IS NULL;

DELETE FROM category WHERE user_id IS NULL;

ALTER TABLE category
    ALTER COLUMN user_id SET NOT NULL;
