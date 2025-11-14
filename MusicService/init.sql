CREATE DATABASE music_music_db ENCODING = 'UTF8';

\c music_music_db;

CREATE TABLE track (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255),
    genre VARCHAR(255),
    duration_sec INTEGER,
	owner_id BIGINT,
    size_bytes BIGINT,
    storage_path VARCHAR(1024),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP
);
