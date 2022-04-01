CREATE SCHEMA decks;

CREATE TABLE users
(
    id TEXT PRIMARY KEY
);

CREATE TABLE decks
(
    id                TEXT PRIMARY KEY,
    author            TEXT REFERENCES users (id),
    is_private        BOOLEAN                     NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    name              TEXT                        NOT NULL,
    short_description TEXT                        NOT NULL,
    description       TEXT                        NOT NULL,
    source_language   VARCHAR(5)                  NOT NULL,
    target_language   VARCHAR(5)                  NOT NULL,
    image_hash        TEXT                                 DEFAULT NULL,
    version           INT                         NOT NULL NOT NULL
);

CREATE TABLE columns
(
    id      TEXT PRIMARY KEY,
    deck    TEXT REFERENCES decks (id),
    name    TEXT     NOT NULL,
    ordinal SMALLINT NOT NULL,
    version INT      NOT NULL
);

-- TODO: Constraint on maximum number of cards
CREATE TABLE cards
(
    id      TEXT PRIMARY KEY,
    deck    TEXT REFERENCES decks (id),
    ordinal SMALLINT NOT NULL,
    version INT      NOT NULL
);

CREATE TABLE card_data
(
    card     TEXT REFERENCES cards (id),
    "column" TEXT REFERENCES columns (id),
    src      TEXT ARRAY,
    version  INT NOT NULL,
    PRIMARY KEY (card, "column")
);

CREATE TABLE attachments
(
    id   TEXT PRIMARY KEY,
    hash TEXT,
    mime TEXT,
    CONSTRAINT attachments_unique UNIQUE (hash, mime)
);

CREATE TABLE card_attachments
(
    attachment TEXT REFERENCES attachments (id),
    card       TEXT REFERENCES cards (id),
    PRIMARY KEY (attachment, card)
);

CREATE TABLE ignored_card
(
    card   TEXT REFERENCES cards (id),
    "user" TEXT REFERENCES users (id),
    PRIMARY KEY (card, "user")
);

CREATE TABLE card_progress
(
    card          TEXT REFERENCES cards (id),
    "user"        TEXT REFERENCES users (id),
    reviewed_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (card, "user", reviewed_date)
);
