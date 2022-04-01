package com.benkyo.decks.data

import net.lecousin.reactive.data.relational.annotations.ForeignTable

data class CardWithData(
    val id: String,
    val deck: String,
    val ordinal: Short,
    @ForeignTable(joinKey = "card")
    val data: Set<CardData>
)
