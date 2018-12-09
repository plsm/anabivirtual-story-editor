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

CREATE TABLE point_of_interest (
  ID INTEGER PRIMARY KEY ASC NOT NULL,
  title TEXT NOT NULL,
  audio_filename TEXT NOT NULL,
  transcription TEXT NOT NULL,
  image_filename TEXT,
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

CREATE VIEW view_point_of_interest (
  location_ID,
  point_of_interest_ID,
  latitude,
  longitude,
  location_name,
  title,
  image_filename,
  audio_filename,
  transcription
) AS SELECT
  location.ID,
  point_of_interest.ID,
  location.latitude,
  location.longitude,
  location.name,
  point_of_interest.title,
  point_of_interest.image_filename,
  point_of_interest.audio_filename,
  point_of_interest.transcription
  FROM location
  INNER JOIN point_of_interest ON location.ID = point_of_interest.location_ID;  
