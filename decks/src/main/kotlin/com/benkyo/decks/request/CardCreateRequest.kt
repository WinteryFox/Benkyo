package com.benkyo.decks.request

data class CardUpdateRequest(
    val ordinal: Short? = null,
    val data: List<CardData>? = null
)

data class CardCreateRequest(
    val ordinal: Short,
    val data: List<CardData>
)

data class CardData(
    val column: String,
    val src: List<String>
)
