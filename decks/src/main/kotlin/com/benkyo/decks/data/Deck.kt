package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("decks")
data class Deck(
    @Id
    val id: String,
    val author: String,
    val isPrivate: Boolean,
    val createdAt: LocalDateTime,
    val name: String,
    val shortDescription: String,
    val description: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val imageHash: String?,
    @JsonIgnore
    @Version
    val version: Int = 0
)
