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
    val tags: Array<String>,

    @Version
    @JsonIgnore
    val version: Int = 0,

    val data: List<CardData> = listOf(),
    val attachments: List<Attachment> = listOf(),
) {
    // region: Generated functions
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (id != other.id) return false
        if (deck != other.deck) return false
        if (ordinal != other.ordinal) return false
        if (!tags.contentEquals(other.tags)) return false
        if (version != other.version) return false
        if (data != other.data) return false
        if (attachments != other.attachments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + deck.hashCode()
        result = 31 * result + ordinal
        result = 31 * result + tags.contentHashCode()
        result = 31 * result + version
        result = 31 * result + data.hashCode()
        result = 31 * result + attachments.hashCode()
        return result
    }
    // endregion
}

// This factory function exists because jOOQ's `into` method for records can't handle finding the right constructor
// when not all the fields have been provided. And yes, I agree, it sucks, and it's not extremely safe. This is
// documented in jOOQ issue #10236, which they've been putting off since mid-2020. A workaround is available, but it's
// not suitable for our approach - it'd make deserializing normally impossible.
// https://github.com/jOOQ/jOOQ/issues/10236

@Suppress("UNCHECKED_CAST")  // Type erasure's a bitch
fun Card(record: Record): Card = Card(
    id = record["id"] as String,
    deck = record["deck"] as String,
    ordinal = record["ordinal"] as Short,
    version = record.getOrNull("version") as Int? ?: 0,
    tags = record.get("tags") as Array<String>,

    data = record.getOrNull("data") as List<CardData>? ?: listOf(),
    attachments = record.getOrNull("attachments") as List<Attachment>? ?: listOf()
)

data class CardWithDataOrdinal(
    val id: String,
    val ordinal: Short,
    val data: List<CardDataWithOrdinal> = listOf(),
    val attachments: List<Attachment> = listOf(),
    val tags: Array<String> = emptyArray(),
)

data class CardsWithData(
    val columns: List<Column> = listOf(),
    val cards: List<CardWithDataOrdinal> = listOf(),
)
