ALTER TABLE video_games.genre ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.genre ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.genre_id_seq;
CREATE SEQUENCE video_games.genre_id_seq;

-- Обновляем столбец для использования последовательности
ALTER TABLE video_games.genre
ALTER COLUMN id SET DEFAULT nextval('video_games.genre_id_seq');

-- Устанавливаем текущее значение последовательности
SELECT setval('video_games.genre_id_seq', (SELECT MAX(id) FROM video_games.genre));

ALTER TABLE video_games.publisher ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.publisher ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.publisher_id_seq;
CREATE SEQUENCE video_games.publisher_id_seq;

ALTER TABLE video_games.publisher
ALTER COLUMN id SET DEFAULT nextval('video_games.publisher_id_seq');

SELECT setval('video_games.publisher_id_seq', (SELECT MAX(id) FROM video_games.publisher));

ALTER TABLE video_games.region ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.region ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.region_id_seq;
CREATE SEQUENCE video_games.region_id_seq;

ALTER TABLE video_games.region
ALTER COLUMN id SET DEFAULT nextval('video_games.region_id_seq');

SELECT setval('video_games.region_id_seq', (SELECT MAX(id) FROM video_games.region));

ALTER TABLE video_games.platform ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.platform ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.platform_id_seq;
CREATE SEQUENCE video_games.platform_id_seq;

ALTER TABLE video_games.platform
ALTER COLUMN id SET DEFAULT nextval('video_games.platform_id_seq');

SELECT setval('video_games.platform_id_seq', (SELECT MAX(id) FROM video_games.platform));


ALTER TABLE video_games.game ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.game ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.game_id_seq;
CREATE SEQUENCE video_games.game_id_seq;

ALTER TABLE video_games.game
ALTER COLUMN id SET DEFAULT nextval('video_games.game_id_seq');

SELECT setval('video_games.game_id_seq', (SELECT MAX(id) FROM video_games.game));

ALTER TABLE video_games.game_publisher ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.game_publisher ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.game_publisher_id_seq;
CREATE SEQUENCE video_games.game_publisher_id_seq;

ALTER TABLE video_games.game_publisher
ALTER COLUMN id SET DEFAULT nextval('video_games.game_publisher_id_seq');

SELECT setval('video_games.game_publisher_id_seq', (SELECT MAX(id) FROM video_games.game_publisher));

ALTER TABLE video_games.game_platform ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.game_platform ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.game_platform_id_seq;
CREATE SEQUENCE video_games.game_platform_id_seq;

ALTER TABLE video_games.game_platform
ALTER COLUMN id SET DEFAULT nextval('video_games.game_platform_id_seq');

SELECT setval('video_games.game_platform_id_seq', (SELECT MAX(id) FROM video_games.game_platform));










