CREATE DATABASE user_recs_db ENCODING = 'UTF8';

\c user_recs_db;

CREATE TABLE user_recs (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    rec_genre VARCHAR(50) NOT NULL,
    recommendation_json VARCHAR(1000) DEFAULT '{}'
);