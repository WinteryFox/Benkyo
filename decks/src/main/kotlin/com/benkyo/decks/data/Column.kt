package com.benkyo.decks.data

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("columns")
data class Column(
    @Id
    val id: String,
    val deck: String,
    val name: String,
    val ordinal: Short,
    @Version
    val version: Int = 0
)
