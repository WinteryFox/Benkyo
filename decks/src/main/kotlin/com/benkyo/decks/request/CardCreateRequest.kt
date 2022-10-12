package com.benkyo.decks.request

data class CardUpdateRequest(
    val ordinal: Short? = null,
    val data: List<CardData>? = null,
    val tags: Array<String> = emptyArray(),
)

data class CardCreateRequest(
    val ordinal: Short,
    val data: List<CardData>,
    val tags: Array<String> = emptyArray(),
)

data class CardData(
    val column: String,
    val src: List<String>,
)
