/**
 * Author:  pedro
 * Created: 16/out/2018
 */

INSERT INTO location (latitude, longitude, name) VALUES (60.2, 56.3, 'Igreja dos Apanhados');
INSERT INTO location (latitude, longitude, name) VALUES (59.98, 57.9, 'Cais dos Destemidos');

INSERT INTO story (title, type, location_id) VALUES ('Alfredo Zangão', 1, 1);
INSERT INTO story (title, type, location_id) VALUES ('Capitão Teutão', 1, 2);

INSERT INTO audio (filename, story_id) VALUES ('alfredo-zangao.mp3', 1);
INSERT INTO audio (filename, story_id) VALUES ('capitao-teutao.mp3', 2);
