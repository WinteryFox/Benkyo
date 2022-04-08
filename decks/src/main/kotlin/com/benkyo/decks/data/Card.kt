package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
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

    // TODO: See CardRepository to figure out how best to do that
//    val attachments: List<Attachment> = listOf(),
)

data class CardWithDataOrdinal(
    val id: String,
    val ordinal: Short,
    val data: List<CardDataWithOrdinal> = listOf(),
)

data class CardsWithData(
    val columns: List<Column> = listOf(),
    val cards: List<CardWithDataOrdinal> = listOf(),
)
