CREATE TABLE location (
  ID INTEGER PRIMARY KEY ASC NOT NULL,
  latitude REAL NOT NULL,
  longitude REAL NOT NULL,
  name TEXT NOT NULL
  );

CREATE TABLE story (
  ID INTEGER PRIMARY KEY ASC NOT NULL,
  title TEXT NOT NULL,
  type INT NOT NULL,
  location_ID INT NOT NULL,
  FOREIGN KEY (location_ID) REFERENCES location (ID) ON DELETE CASCADE
);

CREATE TABLE audio (
  story_ID INTEGER NOT NULL,
  filename TEXT NOT NULL,
  FOREIGN KEY (story_ID) REFERENCES story (ID) ON DELETE CASCADE
);

CREATE INDEX location_index
  ON location (latitude, longitude);

CREATE VIEW audio_story (
  location_ID,
  story_ID,
  latitude,
  longitude,
  name,
  title,
  filename
) AS SELECT
  location.ID,
  story.ID,
  location.latitude,
  location.longitude,
  location.name,
  story.title,
  audio.filename
  FROM location
  INNER JOIN story ON location.ID = story.location_ID
  INNER JOIN audio ON story.ID = audio.story_ID;

