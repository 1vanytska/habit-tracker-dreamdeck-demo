CREATE TABLE IF NOT EXISTS goal
(
    goal_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title TEXT NOT NULL,
    picture TEXT,
    description TEXT,
    category_id UUID NOT NULL,
    is_public BOOLEAN NOT NULL,
    start_date DATE,
    deadline DATE NOT NULL,
    status TEXT NOT NULL,
    is_archived BOOLEAN NOT NULL,
    archiving_time DATE,

    CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_goal_category FOREIGN KEY (category_id) REFERENCES category (id)
    );