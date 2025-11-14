CREATE DATABASE music_search_db ENCODING = 'UTF8';

\c music_search_db;

CREATE TABLE track (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255),
    genre VARCHAR(255)
);
