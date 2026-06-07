CREATE TABLE IF NOT EXISTS app_user
(
    id UUID PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    username TEXT NOT NULL UNIQUE,
    display_name TEXT,
    description TEXT,
    password TEXT NOT NULL,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL,
    profile_picture TEXT,
    role TEXT NOT NULL,
    status TEXT NOT NULL
);