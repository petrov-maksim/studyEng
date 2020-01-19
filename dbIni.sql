CREATE DATABASE v1;
\c v1

CREATE TABLE users(
    id SERIAL PRIMARY KEY,
    name VARCHAR(26) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(30) NOT NULL,
    status BOOLEAN DEFAULT 'false',
    cur_lvl VARCHAR(5) DEFAULT 'A1L1',
    main_wordSet INTEGER REFERENCES wordSets(id)
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
    size INTEGER DEFAULT 0
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