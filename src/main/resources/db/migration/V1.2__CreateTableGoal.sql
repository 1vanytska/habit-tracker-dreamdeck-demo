CREATE TABLE IF NOT EXISTS public."Goal"
(
    goal_id serial PRIMARY KEY,
    user_id integer NOT NULL REFERENCES public."User" (user_id),
    title text NOT NULL,
    picture text,
    description text,
    category_id integer NOT NULL REFERENCES public."Category" (category_id),
    is_public boolean NOT NULL,
    deadline date NOT NULL,
    status text NOT NULL,
    is_archived boolean NOT NULL,
    archivation_time timestamp with time zone
);