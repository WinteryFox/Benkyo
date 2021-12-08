CREATE TABLE users
(
    id          TEXT PRIMARY KEY,
    flags       SMALLINT DEFAULT 0,
    username    TEXT NOT NULL,
    avatar_hash TEXT
);

CREATE TABLE decks
(
    id              TEXT PRIMARY KEY,
    author          TEXT REFERENCES users (id),
    is_private      BOOLEAN                     NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    name            TEXT                        NOT NULL,
    description     TEXT                        NOT NULL,
    source_language VARCHAR(5)                  NOT NULL,
    target_language VARCHAR(5)                  NOT NULL,
    image_hash      TEXT                                 DEFAULT NULL
);

CREATE TABLE cards
(
    id       TEXT PRIMARY KEY,
    deck     TEXT REFERENCES decks (id),
    question TEXT NOT NULL
);

CREATE TABLE answers
(
    card TEXT REFERENCES cards (id),
    src  TEXT,
    PRIMARY KEY (card, src)
);

CREATE TABLE audio
(
    card TEXT REFERENCES cards (id),
    src  TEXT,
    PRIMARY KEY (card, src)
);

CREATE TABLE images
(
    card TEXT REFERENCES cards (id),
    src  TEXT,
    PRIMARY KEY (card, src)
);

CREATE TABLE ignored_cards
(
    card   TEXT REFERENCES cards (id),
    "user" TEXT REFERENCES users (id),
    PRIMARY KEY (card, "user")
);

CREATE TABLE card_progress
(
    deck          TEXT REFERENCES decks (id),
    card          TEXT REFERENCES cards (id),
    "user"        TEXT REFERENCES users (id),
    progress      SMALLINT                    NOT NULL,
    reviewed_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (deck, card, "user")
);
