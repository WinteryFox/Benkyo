package com.benkyo.decks.controller

import com.benkyo.decks.service.DeckService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeckController(
    private val service: DeckService
) {
    @GetMapping("/decks")
    fun getDecks() = service.getAll().map { it.data }

    @GetMapping("/decks/{id}")
    fun getDeck(@PathVariable id: String) = service.getById(id.toLong()).map { it.data }

    @GetMapping("/decks/{id}/cards")
    fun getCards(@PathVariable id: String) = service.getCards(id.toLong())
}
