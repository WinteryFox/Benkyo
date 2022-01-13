package com.benkyo.decks.data

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("cards")
data class Card(
    @Id
    val id: String,
    val deck: String,
    val question: String,
    @Version
    val version: Int
)
