package com.benkyo.decks.data

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("answers")
data class Answer(
    @Id
    val id: String,
    val card: String,
    val src: String,
    @Version
    val version: Int
)
