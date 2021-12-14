package com.benkyo.decks.controller

import com.benkyo.decks.repository.DeckRepository
import kotlinx.coroutines.flow.filter
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/decks")
class DeckController(
    private val deckRepository: DeckRepository
) {
    @GetMapping
    fun getDecks() = deckRepository.findAll().filter { !it.isPrivate }

    @PatchMapping("/{id}")
    fun updateDeck() {

    }

    @GetMapping("/{id}")
    suspend fun getDeck(@PathVariable id: String) = deckRepository.findById(id)
}
