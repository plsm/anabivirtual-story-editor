PRAGMA foreign_keys = true;

CREATE TABLE location (
  ID INTEGER PRIMARY KEY ASC NOT NULL,
  latitude REAL NOT NULL,
  longitude REAL NOT NULL,
  name TEXT NOT NULL
  );

CREATE TABLE story (
  ID INTEGER PRIMARY KEY ASC NOT NULL,
  title TEXT NOT NULL,
  location_ID INT NOT NULL,
  audio_filename TEXT NOT NULL,
  transcription TEXT NOT NULL,
  FOREIGN KEY (location_ID) REFERENCES location (ID) ON DELETE CASCADE
);

CREATE TABLE place (
  ID INTEGER PRIMARY KEY ASC NOT NULL,
  image_filename TEXT NOT NULL,
  location_ID INT NOT NULL,
  FOREIGN KEY (location_ID) REFERENCES location (ID) ON DELETE CASCADE
);

CREATE TABLE background_music (
  ID INTEGER PRIMARY KEY NOT NULL,
  audio_filename TEXT NOT NULL,
  region_center_latitude REAL NOT NULL,
  region_center_longitude REAL NOT NULL,
  region_radius REAL NOT NULL
);

CREATE INDEX location_index
  ON location (latitude, longitude);

CREATE VIEW view_story (
  location_ID,
  story_ID,
  latitude,
  longitude,
  location_name,
  title,
  audio_filename,
  transcription
) AS SELECT
  location.ID,
  story.ID,
  location.latitude,
  location.longitude,
  location.name,
  story.title,
  story.audio_filename,
  story.transcription
  FROM location
  INNER JOIN story ON location.ID = story.location_ID;

CREATE VIEW view_place (
  location_ID,
  place_ID,
  latitude,
  longitude,
  location_name,
  image_filename
) AS SELECT
  location.ID,
  place.ID,
  location.latitude,
  location.longitude,
  location.name,
  place.image_filename
  FROM location
  INNER JOIN place ON location.ID = place.location_ID;  
