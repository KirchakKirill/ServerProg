ALTER TABLE video_games.game
ALTER COLUMN game_name SET NOT NULL,
ADD CONSTRAINT game_name_unique UNIQUE (game_name);

ALTER TABLE video_games.platform
ALTER COLUMN platform_name SET NOT NULL,
ADD CONSTRAINT platform_name_unique UNIQUE (platform_name);

ALTER TABLE video_games.publisher
ALTER COLUMN publisher_name SET NOT NULL,
ADD CONSTRAINT publisher_name_unique UNIQUE (publisher_name);

ALTER TABLE video_games.genre
ALTER COLUMN genre_name SET NOT NULL,
ADD CONSTRAINT genre_name_unique UNIQUE (genre_name);


ALTER TABLE video_games.region
ALTER COLUMN region_name SET NOT NULL,
ADD CONSTRAINT region_name_unique UNIQUE (region_name);
