CREATE DATABASE music_friends_db ENCODING = 'UTF8';

\c music_friends_db;

CREATE TABLE profile (
    user_id BIGSERIAL PRIMARY KEY,
    display_name VARCHAR(255),
    bio TEXT,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
