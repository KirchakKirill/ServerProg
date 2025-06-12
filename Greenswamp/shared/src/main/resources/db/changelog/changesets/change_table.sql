ALTER TABLE users
ADD COLUMN password TEXT NOT NULL
CHECK (
    LENGTH(password) BETWEEN 8 AND 255 AND
    password ~ '[A-Z]' AND  -- хотя бы одна заглавная буква
    password ~ '[a-z]' AND  -- хотя бы одна строчная буква
    password ~ '[0-9]' AND  -- хотя бы одна цифра
    password ~ '[^A-Za-z0-9]'  -- хотя бы один спецсимвол
);

ALTER TABLE users 
ADD COLUMN email TEXT NOT NULL UNIQUE;

CREATE TABLE IF NOT EXISTS authorities (
    id SERIAL PRIMARY KEY,
    authority TEXT NOT NULL CHECK (authority IN ('USER', 'ADMIN','MODERATOR'))
);

CREATE TABLE IF NOT EXISTS users_authorities (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    authority_id INTEGER REFERENCES authorities(id) ON DELETE CASCADE
);

insert into authorities (authority) values ('USER');
insert into authorities (authority) values ('ADMIN');
insert into authorities (authority) values ('MODERATOR');