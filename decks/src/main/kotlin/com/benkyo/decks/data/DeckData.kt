package com.benkyo.decks.data

import org.springframework.data.relational.core.mapping.Table

@Table
data class DeckData(
    val id: Long,
    val author: Long,
    val name: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val imageHash: String?
)
