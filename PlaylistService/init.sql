CREATE DATABASE music_playlist_db ENCODING = 'UTF8';

\c music_playlist_db;

CREATE TABLE playlist(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
	owner_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE playlist_track (
    playlist_id BIGINT NOT NULL REFERENCES playlist(id),
    track_id BIGINT NOT NULL,
    position INTEGER,
    PRIMARY KEY (playlist_id, track_id)
);
