CREATE TABLE IF NOT EXISTS category
(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL
);

INSERT INTO category (id, name) VALUES
                ('d3f8e5c1-2b4a-4d9f-8e1c-3b5a7c9d1e2f', 'Здоров''я'),
                ('a1b2c3d4-e5f6-7890-1234-567890abcdef', 'Фінанси'),
                ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Освіта');