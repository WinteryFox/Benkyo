package com.benkyo.decks.controller

import com.benkyo.decks.data.CardWithAnswers
import com.benkyo.decks.repository.DeckRepository
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.security.Principal

@RestController
@RequestMapping("/study/{deck}")
class StudyController(
    private val deckRepository: DeckRepository
) {
    @GetMapping
    fun getNew(principal: Principal, @PathVariable deck: String): Flux<CardWithAnswers> =
        deckRepository.findNewCardsByDeckAndUser(deck, principal.name)
}
