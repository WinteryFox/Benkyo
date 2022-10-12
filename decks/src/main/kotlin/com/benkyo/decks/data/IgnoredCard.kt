package com.benkyo.decks.data

import org.springframework.data.relational.core.mapping.Table

@Table("ignored_cards")
data class IgnoredCard(
    val card: String,
    val user: String
)
