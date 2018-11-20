/**
 * Author:  pedro
 * Created: 16/out/2018
 */

INSERT INTO location (latitude, longitude, name) VALUES (38.707911, -9.137939, 'Casa da Índia');
INSERT INTO location (latitude, longitude, name) VALUES (38.708386, -9.135485, 'Alfândega');
INSERT INTO location (latitude, longitude, name) VALUES (38.709236, -9.134746, 'Praça dos Escravos');

INSERT INTO story (title, location_id, audio_filename, transcription) VALUES ('Alfredo Zangão', 1, 'alfredo_zangao.mp3', 'Alfredo Zangão viveu contente na sua condição de escravo livre');
INSERT INTO story (title, location_id, audio_filename, transcription) VALUES ('Capitão Teutão', 2, 'capitao_teutao.mp3', 'Capitão Teutão foi um famoso escudeiro');
INSERT INTO story (title, location_id, audio_filename, transcription) VALUES ('El-Rei Manoel', 3, 'el_rei_manoel.mp3', 'El-Rei Manoel queria estar perto das suas mercadorias');

INSERT INTO place (image_filename, location_id) VALUES ('retrato_el_rei_manoel.png', 3);

INSERT INTO background_music (audio_filename, region_center_latitude, region_center_longitude, region_radius) VALUES ('musica_quinhentista.mp3', 38.707911, -9.137939, 10);
