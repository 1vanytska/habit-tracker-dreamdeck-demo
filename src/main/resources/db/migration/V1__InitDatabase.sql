-- Enable pgcrypto for UUID generation
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create Category table
CREATE TABLE IF NOT EXISTS public."category"
(
    category_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL
    );

-- Create User table
CREATE TABLE IF NOT EXISTS public."user"
(
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email text NOT NULL UNIQUE,
    nickname text NOT NULL UNIQUE,
    description text,
    password text NOT NULL,
    registration_date timestamp with time zone NOT NULL,
                                    profile_picture text,
                                    role text NOT NULL,
                                    status text NOT NULL
                                    );

-- Create Goal table
CREATE TABLE IF NOT EXISTS public."goal"
(
    goal_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public."user" (user_id),
    title text NOT NULL,
    picture text,
    description text,
    category_id UUID NOT NULL REFERENCES public."category" (category_id),
    is_public boolean NOT NULL,
    deadline date NOT NULL,
    status text NOT NULL,
    is_archived boolean NOT NULL,
    archivation_time timestamp with time zone
                                   );

-- Create Comment table
CREATE TABLE IF NOT EXISTS public."comment"
(
    comment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    goal_id UUID NOT NULL REFERENCES public."goal" (goal_id),
    user_id UUID NOT NULL REFERENCES public."user" (user_id),
    content text NOT NULL,
    date timestamp with time zone NOT NULL
                       );

-- Create Friendship table
CREATE TABLE IF NOT EXISTS public."friendship"
(
    friendship_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES public."user" (user_id),
    friend_id UUID NOT NULL REFERENCES public."user" (user_id),
    status text NOT NULL
    );

-- Create Complaint table
CREATE TABLE IF NOT EXISTS public."complaint"
(
    complaint_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    complainant_id UUID NOT NULL REFERENCES public."user" (user_id),
    reported_user_id UUID NOT NULL REFERENCES public."user" (user_id),
    description text NOT NULL,
    status text NOT NULL,
    date timestamp with time zone NOT NULL
                       );

-- Create GoalNote table
CREATE TABLE IF NOT EXISTS public."goalnote"
(
    note_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    goal_id UUID NOT NULL REFERENCES public."goal" (goal_id),
    date timestamp with time zone NOT NULL,
                       content text NOT NULL
                       );

-- Create GoalStep table
CREATE TABLE IF NOT EXISTS public."goalstep"
(
    step_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    goal_id UUID NOT NULL REFERENCES public."goal" (goal_id),
    description text NOT NULL,
    is_done boolean NOT NULL,
    completion_date timestamp
    );

-- Create Like table
CREATE TABLE IF NOT EXISTS public."like"
(
    user_id UUID NOT NULL REFERENCES public."user" (user_id),
    goal_id UUID NOT NULL REFERENCES public."goal" (goal_id),
    PRIMARY KEY (user_id, goal_id)
    );

-- Create View table
CREATE TABLE IF NOT EXISTS public."view"
(
    user_id UUID NOT NULL REFERENCES public."user" (user_id),
    goal_id UUID NOT NULL REFERENCES public."goal" (goal_id),
    PRIMARY KEY (user_id, goal_id)
    );
