package com.benkyo.decks.controller

import com.benkyo.decks.repository.CardRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/decks/{id}/cards")
class CardController(
    private val cardRepository: CardRepository
) {
    @GetMapping
    fun getCards(@PathVariable id: String) = cardRepository.findAllByDeck(id)
}
