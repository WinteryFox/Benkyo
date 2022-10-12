package com.benkyo.decks.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("columns")
data class Column(
    @Id
    val id: String,
    @JsonIgnore
    val deck: String,
    val name: String,
    val ordinal: Short,
    @JsonIgnore
    @Version
    val version: Int = 0
)
