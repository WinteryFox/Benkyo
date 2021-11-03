package com.benkyo.decks.data

data class Card(
    val id: Long,
    val question: String,
    val answers: List<String>
)
