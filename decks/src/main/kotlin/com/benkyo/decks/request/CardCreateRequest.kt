package com.benkyo.decks.request

data class CardCreateRequest(
    val ordinal: Short,
    val data: List<CardData>
)

data class CardData(
    val column: String,
    val src: List<String>
)
