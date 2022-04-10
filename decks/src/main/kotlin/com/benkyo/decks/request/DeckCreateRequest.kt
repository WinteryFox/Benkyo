package com.benkyo.decks.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class DeckCreateRequest(
    @field:NotBlank
    @field:Size(min = 4, max = 64)
    val name: String,
    val isPrivate: Boolean,
    @field:NotBlank
    @field:Size(min = 4, max = 128)
    val shortDescription: String,
    @field:NotBlank
    @field:Size(min = 4, max = 2048)
    val description: String,
    @field:NotBlank
    @field:Size(min = 5, max = 5)
    val sourceLanguage: String,
    @field:NotBlank
    @field:Size(min = 5, max = 5)
    val targetLanguage: String
)
