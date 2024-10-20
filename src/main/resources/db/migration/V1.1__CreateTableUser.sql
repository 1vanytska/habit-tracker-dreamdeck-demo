CREATE TABLE IF NOT EXISTS public."User"
(
    user_id serial PRIMARY KEY,
    email text NOT NULL UNIQUE,
    nickname text NOT NULL UNIQUE,
    password text NOT NULL,
    registration_date timestamp with time zone NOT NULL,
    profile_picture text,
    role text NOT NULL,
    status text NOT NULL
);
