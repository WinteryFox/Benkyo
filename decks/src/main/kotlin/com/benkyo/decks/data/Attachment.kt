package com.benkyo.decks.data

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("attachments")
data class Attachment(
    @Id
    val id: String,
    val hash: String,
    val mime: String
)
