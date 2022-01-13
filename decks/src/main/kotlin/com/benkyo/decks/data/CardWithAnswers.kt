package com.benkyo.decks.data

data class CardWithAnswers(
    val id: String,
    val deck: String,
    val question: String,
    val answers: List<String>
)
