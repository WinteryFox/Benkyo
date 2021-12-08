package com.benkyo.decks.data

data class Card(
    val id: String,
    val deck: String,
    val question: String,
    val answers: List<String>
)
