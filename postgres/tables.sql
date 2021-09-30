CREATE TABLE users
(
    id       BIGINT PRIMARY KEY,
    username TEXT NOT NULL,
    hash     TEXT
);

CREATE TABLE decks
(
    id       BIGINT PRIMARY KEY,
    author   BIGINT REFERENCES users (id),
    name     TEXT       NOT NULL,
    language VARCHAR(5) NOT NULL,
    hash     TEXT
);

CREATE TABLE cards
(
    id   BIGINT PRIMARY KEY,
    deck BIGINT REFERENCES decks (id),
    src  TEXT NOT NULL,
    dest TEXT NOT NULL
);

CREATE TABLE audio
(
    card BIGINT REFERENCES cards (id),
    hash TEXT,
    PRIMARY KEY (card, hash)
);

CREATE TABLE images
(
    card BIGINT REFERENCES cards (id),
    hash TEXT,
    PRIMARY KEY (card, hash)
);
