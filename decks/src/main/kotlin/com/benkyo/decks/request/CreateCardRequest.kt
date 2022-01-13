package com.benkyo.decks.request

data class CreateCardRequest(
    val question: String,
    val answers: List<String>
)
