

ALTER TABLE video_games.t_user ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.t_user ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.t_user_id_seq;


CREATE SEQUENCE video_games.t_user_id_seq;

ALTER TABLE video_games.t_user
ALTER COLUMN id SET DEFAULT nextval('video_games.t_user_id_seq');

SELECT setval('video_games.t_user_id_seq', (SELECT MAX(id) FROM video_games.t_user),true);



ALTER TABLE video_games.t_user_authority ALTER COLUMN id DROP IDENTITY;
ALTER TABLE video_games.t_user_authority ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS video_games.t_user_authority_id_seq;


CREATE SEQUENCE video_games.t_user_authority_id_seq;

ALTER TABLE video_games.t_user_authority
ALTER COLUMN id SET DEFAULT nextval('video_games.t_user_authority_id_seq');

SELECT setval('video_games.t_user_authority_id_seq', (SELECT MAX(id) FROM video_games.t_user_authority),true);