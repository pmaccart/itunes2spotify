DROP DATABASE IF EXISTS itunes2spotify;

CREATE DATABASE IF NOT EXISTS itunes2spotify;
USE itunes2spotify;

CREATE TABLE IF NOT EXISTS session (
  session_id varchar(128) NOT NULL,
  session_data varchar(1024)
);

ALTER TABLE session ADD PRIMARY KEY (session_id);


CREATE TABLE IF NOT EXISTS tracks (
  track_id varchar(32) NOT NULL,
  name varchar(1024),
  artist varchar(1024),
  album varchar(1024),
  year INTEGER,
  genre varchar(512),
  playcount INTEGER,
  spotify_uri varchar(128)
);

ALTER TABLE tracks ADD PRIMARY KEY (track_id);
  

CREATE TABLE IF NOT EXISTS playlists (
  playlist_id varchar(32) NOT NULL,
  spotify_id varchar(64),
  name varchar(1024) NOT NULL
);

ALTER TABLE playlists ADD PRIMARY KEY (playlist_id);

CREATE TABLE IF NOT EXISTS playlist_tracks (
  playlist_id varchar(32),
  track_id varchar(32)
); 

ALTER TABLE playlist_tracks ADD FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id);
ALTER TABLE playlist_tracks ADD FOREIGN KEY (track_id) REFERENCES tracks(track_id);

GRANT ALL ON itunes2spotify2.* to 'itunes2spotify2'@'localhost';