package com.benkyo.decks.data

import org.springframework.data.relational.core.mapping.Table

@Table("card_attachments")
data class CardAttachment(
    val attachment: String,
    val card: String
)
