package com.benkyo.decks.data

import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("answers")
data class Answer(
    val card: String,
    val src: String,
    @Version
    val version: Int = 0
)
