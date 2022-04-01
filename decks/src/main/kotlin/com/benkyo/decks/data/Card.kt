package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
import net.lecousin.reactive.data.relational.annotations.ForeignTable
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("cards")
data class Card(
    @Id
    val id: String,
    @JsonIgnore
    val deck: String,
    val ordinal: Short,
    @Version
    @JsonIgnore
    val version: Int = 0
) {
    @ForeignTable(joinKey = "card", optional = true)
    val data: Set<CardData> = setOf()
}
