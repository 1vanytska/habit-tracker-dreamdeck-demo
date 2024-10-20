-- Create the Comment table
CREATE TABLE IF NOT EXISTS public."Comment"
(
    comment_id serial PRIMARY KEY,
    goal_id integer NOT NULL REFERENCES public."Goal" (goal_id),
    user_id integer NOT NULL REFERENCES public."User" (user_id),
    content text NOT NULL,
    date timestamp with time zone NOT NULL
);

-- Create the Friendship table
CREATE TABLE IF NOT EXISTS public."Friendship"
(
    friendship_id serial PRIMARY KEY,
    user_id integer NOT NULL REFERENCES public."User" (user_id),
    friend_id integer NOT NULL REFERENCES public."User" (user_id),
    status text NOT NULL
);

-- Create the Complaint table
CREATE TABLE IF NOT EXISTS public."Complaint"
(
    complaint_id serial PRIMARY KEY,
    complainant_id integer NOT NULL REFERENCES public."User" (user_id),
    reported_user_id integer NOT NULL REFERENCES public."User" (user_id),
    description text NOT NULL,
    status text NOT NULL,
    date timestamp with time zone NOT NULL
);

-- Create the GoalNote table
CREATE TABLE IF NOT EXISTS public."GoalNote"
(
    note_id serial PRIMARY KEY,
    goal_id integer NOT NULL REFERENCES public."Goal" (goal_id),
    date timestamp with time zone NOT NULL,
    content text NOT NULL
);

-- Create the GoalStep table
CREATE TABLE IF NOT EXISTS public."GoalStep"
(
    step_id serial PRIMARY KEY,
    goal_id integer NOT NULL REFERENCES public."Goal" (goal_id),
    description text NOT NULL,
    is_done boolean NOT NULL,
    completion_date timestamp
);

-- Create the Like table
CREATE TABLE IF NOT EXISTS public."Like"
(
    user_id integer NOT NULL REFERENCES public."User" (user_id),
    goal_id integer NOT NULL REFERENCES public."Goal" (goal_id),
    PRIMARY KEY (user_id, goal_id)
);

-- Create the View table
CREATE TABLE IF NOT EXISTS public."View"
(
    user_id integer NOT NULL REFERENCES public."User" (user_id),
    goal_id integer NOT NULL REFERENCES public."Goal" (goal_id),
    PRIMARY KEY (user_id, goal_id)
);