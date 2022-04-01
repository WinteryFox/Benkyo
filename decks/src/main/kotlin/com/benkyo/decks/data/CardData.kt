package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
import net.lecousin.reactive.data.relational.annotations.ForeignKey
import org.springframework.data.relational.core.mapping.Table

@Table("card_data")
data class CardData(
    @JsonIgnore
    @ForeignKey
    val card: Card,
    val column: String,
    val src: List<String>,
    @JsonIgnore
    val version: Int
)
