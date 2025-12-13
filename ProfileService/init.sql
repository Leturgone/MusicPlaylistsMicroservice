CREATE DATABASE music_profile_db ENCODING = 'UTF8';

\c music_profile_db;

CREATE TABLE profile (
    user_id BIGSERIAL PRIMARY KEY,
    display_name VARCHAR(255),
    bio TEXT,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
