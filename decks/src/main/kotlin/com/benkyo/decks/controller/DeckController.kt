package com.benkyo.decks.controller

import com.benkyo.decks.service.DeckService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DeckController(
    val service: DeckService
) {
    @GetMapping("/decks")
    fun getAllDecks() = service.getAll().map { it.data }
}
