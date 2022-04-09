package com.benkyo.decks.data

import com.benkyo.decks.utils.getOrNull
import com.fasterxml.jackson.annotation.JsonIgnore
import org.jooq.Record
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("cards")
data class Card(
    @Id
    val id: String,
    @JsonIgnore
    val deck: String,
    val ordinal: Short,
    @Version
    @JsonIgnore
    val version: Int = 0,

    val data: List<CardData> = listOf(),
    val attachments: List<Attachment> = listOf(),
)

// This factory function exists because jOOQ's `into` method for records can't handle finding the right constructor
// when not all the fields have been provided. And yes, I agree, it sucks, and it's not extremely safe. This is
// documented in jOOQ issue #10236, which they've been putting off since mid-2020. A workaround is available, but it's
// not suitable for our approach - it'd make deserializing normally impossible.
// https://github.com/jOOQ/jOOQ/issues/10236

@Suppress("UNCHECKED_CAST")  // Type erasure's a bitch
fun Card(record: Record) = Card(
    id = record["id"] as String,
    deck = record["deck"] as String,
    ordinal = record["ordinal"] as Short,
    version = record.getOrNull("version") as Int? ?: 0,
    data = record.getOrNull("data") as List<CardData>? ?: listOf(),
    attachments = record.getOrNull("attachments") as List<Attachment>? ?: listOf()
)

data class CardWithDataOrdinal(
    val id: String,
    val ordinal: Short,
    val data: List<CardDataWithOrdinal> = listOf(),
    val attachments: List<Attachment> = listOf()
)

data class CardsWithData(
    val columns: List<Column> = listOf(),
    val cards: List<CardWithDataOrdinal> = listOf(),
)
