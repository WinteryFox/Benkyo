package com.benkyo.decks.data

import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("card_progress")
data class CardProgress(
    val deck: String,
    val card: String,
    val user: String,
    val progress: Short,
    val reviewedDate: LocalDateTime
)
