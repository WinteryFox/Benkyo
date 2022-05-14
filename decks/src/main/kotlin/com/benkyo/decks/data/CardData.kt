package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("card_data")
data class CardData(
    @JsonIgnore
    @Id
    val card: String,
    val column: String,
    val src: List<String>,

    @JsonIgnore
    val version: Int = 0,
)
