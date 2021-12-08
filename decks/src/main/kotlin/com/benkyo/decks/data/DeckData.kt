package com.benkyo.decks.data

import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
data class DeckData(
    val id: String,
    val author: String,
    val isPrivate: Boolean,
    val name: String,
    val description: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val createdAt: LocalDateTime,
    val imageHash: String?
)
