CREATE DATABASE music_friends_db ENCODING = 'UTF8';

\c music_friends_db;

CREATE TABLE friends (
    user_id BIGINT NOT NULL,
    friend_user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, friend_user_id),
    UNIQUE (user_id, friend_user_id)
);
