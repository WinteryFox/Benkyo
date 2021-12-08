package com.benkyo.decks.controller

import com.benkyo.decks.data.Card
import com.benkyo.decks.service.DeckService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/decks")
class DeckController(
    private val service: DeckService
) {
    @GetMapping
    fun getDecks() = service.getAll().filter { !it.data.isPrivate }.map { it.data }

    @GetMapping("/{id}")
    fun getDeck(@PathVariable id: String) = service.getById(id).map { it.data }

    // TODO: Check if deck is private
    @GetMapping("/{id}/cards")
    fun getCards(@PathVariable id: String): Flux<Card> = service.getCards(id)
}
