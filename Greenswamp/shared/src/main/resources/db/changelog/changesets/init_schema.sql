--liquibase formatted sql

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    display_name TEXT NOT NULL,
    avatar_url TEXT,
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- Posts Table (with embedded media)
CREATE TABLE IF NOT EXISTS posts (
    post_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    post_type TEXT NOT NULL CHECK (post_type IN ('text', 'image', 'video')),
    media_url TEXT,
    media_type TEXT CHECK (media_type IN ('image', 'video')),
    alt_text TEXT,
    thumbnail_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    parent_post_id INTEGER REFERENCES posts(post_id),
    CHECK (
        (post_type = 'text' AND media_url IS NULL) OR
        (post_type IN ('image', 'video') AND media_url IS NOT NULL)
    )
);

-- Tags Table
CREATE TABLE IF NOT EXISTS tags (
    tag_id SERIAL PRIMARY KEY,
    tag_name TEXT UNIQUE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    usage_count INTEGER DEFAULT 0
);

-- Post-Tags Junction Table
CREATE TABLE IF NOT EXISTS post_tags (
    post_id INTEGER REFERENCES posts(post_id) ON DELETE CASCADE,
    tag_id INTEGER REFERENCES tags(tag_id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

-- Interactions Table
CREATE TABLE IF NOT EXISTS interactions (
    interaction_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    post_id INTEGER NOT NULL REFERENCES posts(post_id) ON DELETE CASCADE,
    interaction_type TEXT NOT NULL CHECK (
        interaction_type IN ('comment', 'reribb', 'like', 'rsvp')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    content TEXT,
    UNIQUE(user_id, post_id, interaction_type)
);

-- Events Table
CREATE TABLE IF NOT EXISTS events (
    event_id SERIAL PRIMARY KEY,
    post_id INTEGER UNIQUE REFERENCES posts(post_id) ON DELETE CASCADE,
    event_time TIMESTAMP WITH TIME ZONE NOT NULL,
    location TEXT NOT NULL,
    host_org TEXT,
    rsvp_count INTEGER DEFAULT 0,
    max_capacity INTEGER
);

-- Authentication Table
CREATE TABLE IF NOT EXISTS auth (
    user_id INTEGER PRIMARY KEY REFERENCES users(user_id) ON DELETE CASCADE,
    password_hash TEXT NOT NULL,
    last_login TIMESTAMP WITH TIME ZONE,
    reset_token TEXT,
    token_expiry TIMESTAMP WITH TIME ZONE
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_posts_created ON posts(created_at);
CREATE INDEX IF NOT EXISTS idx_posts_type ON posts(post_type);
CREATE INDEX IF NOT EXISTS idx_interactions_type ON interactions(interaction_type);
CREATE INDEX IF NOT EXISTS idx_tags_name ON tags(tag_name);
CREATE INDEX IF NOT EXISTS idx_events_time ON events(event_time);

-- Trending Ponds View (Updated hourly via application logic)
CREATE OR REPLACE VIEW trending_ponds AS
SELECT t.tag_id, t.tag_name, COUNT(pt.post_id) AS recent_posts
FROM tags t
JOIN post_tags pt ON t.tag_id = pt.tag_id
JOIN posts p ON pt.post_id = p.post_id
WHERE p.created_at > (NOW() - INTERVAL '1 day')
GROUP BY t.tag_id, t.tag_name
ORDER BY recent_posts DESC
LIMIT 10;
