CREATE DATABASE v2 WITH ENCODING 'UTF8';
\c v2

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(26) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    status BOOLEAN DEFAULT 'false',
    cur_lvl VARCHAR(5) DEFAULT 'A1L1'
);

CREATE TABLE translations(
    id SERIAL PRIMARY KEY,
    translation text NOT NULL UNIQUE
);

CREATE TABLE words(
    id SERIAL PRIMARY KEY,
    word text NOT NULL UNIQUE
);

CREATE TABLE wordSets(
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id),
    name TEXT NOT NULL,
    isMain BOOLEAN DEFAULT false,
    size INTEGER DEFAULT 0,
    img_path TEXT NOT NULL
);

CREATE TABLE trainings(
    id SMAllSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE examples(
    id SERIAL PRIMARY KEY,
    example TEXT NOT NULL UNIQUE
);


CREATE TABLE user_word_translation(
    user_id INTEGER REFERENCES users(id),
    word_id INTEGER REFERENCES words(id),
    translation_id INTEGER REFERENCES translations(id),
    CONSTRAINT user_word_translation_unique UNIQUE(user_id, word_id, translation_id)
);

CREATE TABLE wordSet_word(
    wordSet_id INTEGER REFERENCES wordSets(id),
    word_id INTEGER REFERENCES words(id),
    index serial,
    CONSTRAINT one_wordSet_one_word UNIQUE(wordSet_id, word_id)
);

CREATE TABLE user_word_training(
    user_id INTEGER REFERENCES users(id),
    word_id INTEGER REFERENCES words(id),
    training_id INTEGER REFERENCES trainings(id),
    isLearned BOOLEAN DEFAULT 'false',
    CONSTRAINT user_word_training_unique UNIQUE(user_id, word_id, training_id)
);

CREATE TABLE user_word_example(
    user_id INTEGER REFERENCES users(id),
    word_id INTEGER REFERENCES words(id),
    example_id INTEGER REFERENCES examples(id),
    CONSTRAINT user_word_unique UNIQUE(user_id, word_id)
);

CREATE TABLE content_text(
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL UNIQUE,
    size SMALLINT NOT NULL,
    rating INTEGER DEFAULT '1',
    name text NOT NULL,
    index SERIAL
);

CREATE TABLE user_content_text(
    user_id INTEGER REFERENCES users(id),
    text_id INTEGER REFERENCES content_text(id),
    cur_page SMALLINT DEFAULT '1',
    isLiked BOOLEAN DEFAULT 'f',
    isLearned BOOLEAN DEFAULT 'f',
    added BOOLEAN DEFAULT 'f',
    index SERIAL,
    CONSTRAINT user_text_unique UNIQUE(user_id, text_id)
);

CREATE TABLE content_video(
    id SERIAL PRIMARY KEY,
    link TEXT NOT NULL UNIQUE,
    name text NOT NULL,
    rating INTEGER DEFAULT '1',
    index SERIAL
);

CREATE TABLE user_content_video(
    user_id INTEGER REFERENCES users(id),
    video_id INTEGER REFERENCES content_video(id),
    isLiked BOOLEAN DEFAULT 'f',
    isLearned BOOLEAN DEFAULT 'f',
    added BOOLEAN DEFAULT 'f',
    index SERIAL,
    CONSTRAINT user_video_unique UNIQUE(user_id, video_id)
);

ALTER TABLE users ADD main_wordSet INTEGER REFERENCES wordSets(id);
INSERT INTO users(name, email, password) VALUES ('Ivan', 'ivanov_ivan@mail.ru', '123');
INSERT INTO wordSets(user_id, name, isMain) VALUES (1, 'All words', true);
INSERT INTO words(word) values ('apple');
INSERT INTO translations(translation) values ('яблоко');
INSERT INTO user_word_translation(user_id, word_id, translation_id) values(1, 1, 1);
INSERT INTO wordSet_word(wordSet_id, word_id) values(1, 1);