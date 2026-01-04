CREATE TABLE IF NOT EXISTS comment
(
    comment_id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goal (goal_id),
    user_id UUID NOT NULL REFERENCES app_user (id),
    content TEXT NOT NULL,
    date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS friendship
(
    friendship_id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES app_user (id),
    friend_id UUID NOT NULL REFERENCES app_user (id),
    status TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS complaint
(
    complaint_id UUID PRIMARY KEY,
    complainant_id UUID NOT NULL REFERENCES app_user (id),
    reported_user_id UUID NOT NULL REFERENCES app_user (id),
    description TEXT NOT NULL,
    status TEXT NOT NULL,
    date TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS goal_note
(
    note_id UUID PRIMARY KEY,
    goal_id UUID NOT NULL REFERENCES goal (goal_id),
    date TIMESTAMP WITH TIME ZONE NOT NULL,
                       content TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS step
(
    step_id UUID PRIMARY KEY,
    description TEXT NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    goal_id UUID NOT NULL,

    CONSTRAINT fk_step_goal FOREIGN KEY (goal_id) REFERENCES goal (goal_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_likes
(
    user_id UUID NOT NULL REFERENCES app_user (id),
    goal_id UUID NOT NULL REFERENCES goal (goal_id),
    PRIMARY KEY (user_id, goal_id)
);

CREATE TABLE IF NOT EXISTS goal_view
(
    user_id UUID NOT NULL REFERENCES app_user (id),
    goal_id UUID NOT NULL REFERENCES goal (goal_id),
    PRIMARY KEY (user_id, goal_id)
);