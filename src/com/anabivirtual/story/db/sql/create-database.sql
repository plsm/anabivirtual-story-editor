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
  FOREIGN KEY (location_ID) REFERENCES location (ID) ON DELETE CASCADE
);

CREATE TABLE audio (
  story_ID INTEGER PRIMARY KEY NOT NULL,
  filename TEXT NOT NULL,
  FOREIGN KEY (story_ID) REFERENCES story (ID) ON DELETE CASCADE
);

CREATE TABLE audio_book (
  story_ID INTEGER PRIMARY KEY NOT NULL,
  filename TEXT NOT NULL,
  transcription TEXT NOT NULL,
  FOREIGN KEY (story_ID) REFERENCES story (ID) ON DELETE CASCADE
);

CREATE TABLE historical_image (
  story_ID INTEGER PRIMARY KEY NOT NULL,
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

CREATE VIEW audio_book_story (
  location_ID,
  story_ID,
  latitude,
  longitude,
  name,
  title,
  filename,
  transcription
) AS SELECT
  location.ID,
  story.ID,
  location.latitude,
  location.longitude,
  location.name,
  story.title,
  audio_book.filename,
  audio_book.transcription
  FROM location
  INNER JOIN story ON location.ID = story.location_ID
  INNER JOIN audio_book ON story.ID = audio_book.story_ID;

CREATE VIEW historical_image_story (
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
  historical_image.filename
  FROM location
  INNER JOIN story ON location.ID = story.location_ID
  INNER JOIN historical_image ON story.ID = historical_image.story_ID;
