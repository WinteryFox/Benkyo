package com.benkyo.decks.controller

import com.benkyo.decks.data.Card
import com.benkyo.decks.repository.CardController
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import java.security.Principal

@RestController
@RequestMapping("/study")
class StudyController(
    private val cardController: CardController
) {
    @GetMapping("/{deck}")
    fun new(principal: Principal, @PathVariable deck: String): Flux<Card> {
        println(principal.name)
        println(deck)
        return cardController.findNewCardsByDeckAndUser(deck, principal.name)
    }
}
