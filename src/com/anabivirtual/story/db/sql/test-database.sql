/**
 * Author:  pedro
 * Created: 16/out/2018
 */

INSERT INTO location (latitude, longitude, name) VALUES (38.707911, -9.137939, 'Casa da Índia');
INSERT INTO location (latitude, longitude, name) VALUES (38.708386, -9.135485, 'Alfândega');
INSERT INTO location (latitude, longitude, name) VALUES (38.709236, -9.134746, 'Praça dos Escravos');

INSERT INTO story (title, location_id) VALUES ('Alfredo Zangão', 1);
INSERT INTO story (title, location_id) VALUES ('Capitão Teutão', 2);
INSERT INTO story (title, location_id) VALUES ('El-Rei Manoel', 3);

INSERT INTO audio (filename, story_id) VALUES ('alfredo_zangao.mp3', 1);
INSERT INTO audio (filename, story_id) VALUES ('capitao_teutao.mp3', 2);

INSERT INTO audio_book (filename, transcription, story_id) VALUES ('el_rei_manoel.mp3', 'El-Rei Manoel queria estar perto das suas mercadorias', 3);

INSERT INTO historical_image (filename, story_id) VALUES ('retrato_el_rei_manoel.png', 3);

INSERT INTO background_music (filename, region_center_latitude, region_center_longitude, region_radius) VALUES ('musica_quinhentista.mp3', 38.707911, -9.137939, 10);
