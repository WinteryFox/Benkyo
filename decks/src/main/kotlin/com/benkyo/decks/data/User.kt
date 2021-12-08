package com.benkyo.decks.data

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    @Id
    val id: String,
    val flags: Short,
    val avatarHash: String?
)
