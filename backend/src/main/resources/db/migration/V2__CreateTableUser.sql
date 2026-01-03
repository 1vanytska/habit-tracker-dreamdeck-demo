CREATE TABLE IF NOT EXISTS app_user
(
    id UUID PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    nickname TEXT NOT NULL UNIQUE,
    description TEXT,
    password TEXT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    profile_picture TEXT,
    role TEXT NOT NULL,
    status TEXT NOT NULL
);

INSERT INTO app_user (id, email, nickname, password, registration_date, role, status)
VALUES ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'test@test.com', 'TestUser', 'password', NOW(), 'USER', 'ACTIVE');