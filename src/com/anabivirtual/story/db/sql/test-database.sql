/**
 * Author:  pedro
 * Created: 16/out/2018
 */

INSERT INTO location (latitude, longitude, name) VALUES (38.707911, -9.137939, 'Casa da Índia');
INSERT INTO location (latitude, longitude, name) VALUES (38.708386, -9.135485, 'Alfândega');
INSERT INTO location (latitude, longitude, name) VALUES (38.709236, -9.134746, 'Praça dos Escravos');

INSERT INTO story (title, type, location_id) VALUES ('Alfredo Zangão', 1, 1);
INSERT INTO story (title, type, location_id) VALUES ('Capitão Teutão', 1, 2);

INSERT INTO audio (filename, story_id) VALUES ('alfredo_zangao.mp3', 1);
INSERT INTO audio (filename, story_id) VALUES ('capitao_teutao.mp3', 2);
