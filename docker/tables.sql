CREATE SCHEMA decks;

CREATE TABLE users
(
    id TEXT PRIMARY KEY
);

CREATE TABLE decks
(
    id                TEXT PRIMARY KEY,
    author            TEXT,
    is_private        BOOLEAN                     NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT current_timestamp,
    name              TEXT                        NOT NULL,
    short_description TEXT                        NOT NULL,
    description       TEXT                        NOT NULL,
    source_language   VARCHAR(5)                  NOT NULL,
    target_language   VARCHAR(5)                  NOT NULL,
    image_hash        TEXT                                 DEFAULT NULL,
    tags              TEXT[]                      NOT NULL DEFAULT '{}',
    version           INT                         NOT NULL,

    FOREIGN KEY (author) REFERENCES users (id) ON DELETE SET NULL
);

CREATE INDEX decks_tags ON decks USING gin(tags);

CREATE TABLE columns
(
    id      TEXT PRIMARY KEY,
    deck    TEXT,
    name    TEXT     NOT NULL,
    ordinal SMALLINT NOT NULL,
    version INT      NOT NULL,

    FOREIGN KEY (deck) REFERENCES decks (id) ON DELETE CASCADE
);

CREATE TABLE cards
(
    id      TEXT PRIMARY KEY,
    deck    TEXT,
    ordinal SMALLINT NOT NULL,
    version INT      NOT NULL,
    tags    TEXT[]   NOT NULL DEFAULT '{}',

    FOREIGN KEY (deck) REFERENCES decks (id) ON DELETE CASCADE
);

CREATE INDEX cards_tags ON cards USING gin(tags);

CREATE TABLE card_data
(
    card     TEXT,
    "column" TEXT,
    src      TEXT ARRAY,
    version  INT NOT NULL,

    PRIMARY KEY (card, "column"),

    FOREIGN KEY (card) REFERENCES cards (id) ON DELETE CASCADE,
    FOREIGN KEY ("column") REFERENCES columns (id) ON DELETE CASCADE,

    CONSTRAINT fk_card FOREIGN KEY (card) REFERENCES cards (id)
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
    attachment TEXT,
    card       TEXT,

    PRIMARY KEY (attachment, card),

    FOREIGN KEY (attachment) REFERENCES attachments (id) ON DELETE CASCADE,
    FOREIGN KEY (card) REFERENCES cards (id) ON DELETE CASCADE
);

CREATE TABLE ignored_card
(
    card   TEXT,
    "user" TEXT,

    PRIMARY KEY (card, "user"),

    FOREIGN KEY (card) REFERENCES cards (id) ON DELETE CASCADE,
    FOREIGN KEY ("user") REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE card_progress
(
    card          TEXT,
    "user"        TEXT,
    reviewed_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    PRIMARY KEY (card, "user", reviewed_date),

    FOREIGN KEY (card) REFERENCES cards (id) ON DELETE CASCADE,
    FOREIGN KEY ("user") REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE tags
(
    tag    TEXT    NOT NULL,
    count  BIGINT  NOT NULL DEFAULT 1,

    PRIMARY KEY (tag)
)
