package com.benkyo.decks.controller

import com.benkyo.decks.repository.DeckRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DeckController(
    val repository: DeckRepository
) {
    @GetMapping("/decks")
    fun getAllDecks() = repository.getAll()
}
